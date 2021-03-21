package io.github.gianpamx.shscale.background

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import io.github.gianpamx.shscale.MainActivity
import io.github.gianpamx.shscale.R
import io.github.gianpamx.shscale.app.ComponentApp
import io.github.gianpamx.shscale.background.BackgroundEvent.ACTIVITY_STARTED
import io.github.gianpamx.shscale.domain.ObserveSettings
import io.github.gianpamx.shscale.domain.SaveSettings
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

const val BG_SERVICE_EVENT = "BG_SERVICE_EVENT"

private const val FOREGROUND_NOTIFICATION_ID = 52265
private const val PERMISSION_NOTIFICATION_ID = 39222
private const val CHANNEL_ID = "BACKGROUND_CHANNEL"

enum class BackgroundEvent {
    ACTIVITY_STARTED,
    ACTIVITY_STOPPED
}

class BackgroundService : LifecycleService() {
    @Inject
    lateinit var backgroundModel: BackgroundModel

    private var isBackgroundServiceEnabled: Boolean? = null

    override fun onCreate() {
        super.onCreate()

        (application as ComponentApp).component.inject(this)

        backgroundModel
            .settings
            .onEach {
                isBackgroundServiceEnabled = it.isBackgroundServiceEnabled
            }
            .launchIn(lifecycleScope)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val event = intent?.extras?.let { extras ->
            extras.getString(BG_SERVICE_EVENT)?.let { BackgroundEvent.valueOf(it) }
        }

        return when {
            event == ACTIVITY_STARTED -> {
                hideForegroundNotification()
                hidePermissionNotification()
                START_NOT_STICKY
            }
            isBackgroundServiceEnabled == false -> {
                hideForegroundNotification()
                hidePermissionNotification()
                stopSelf(startId)
                START_NOT_STICKY
            }
            isPermissionGranted().not() -> {
                askForPermission()
                hideForegroundNotification()
                stopSelf(startId)
                START_NOT_STICKY
            }
            else -> {
                goToForeground()
                START_STICKY
            }
        }
    }

    private fun goToForeground() {
        notifyForeground().also { startForeground(FOREGROUND_NOTIFICATION_ID, it) }
    }

    private fun notifyForeground(): Notification {
        val pendingIntent = Intent(this, MainActivity::class.java).let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) registerChannel()

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_weight_scale)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setContentTitle(getString(R.string.channel_name))
            .build()
            .also {
                NotificationManagerCompat.from(this).notify(FOREGROUND_NOTIFICATION_ID, it)
            }
    }

    private fun askForPermission() {
        val pendingIntent = Intent(this, MainActivity::class.java).let {
            PendingIntent.getActivity(this, 0, it, 0)
        }
        val turnOffPendingIntent = Intent(this, TurnOffBroadcastReceiver::class.java).let {
            PendingIntent.getBroadcast(this, 0, it, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) registerChannel()

        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_weight_scale)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setContentTitle(getString(R.string.background_permission_ask_title))
            .setContentText(getString(R.string.background_permission_ask_description))
            .addAction(
                R.drawable.ic_baseline_block_24,
                getString(R.string.background_permission_ask_turn_off),
                turnOffPendingIntent
            )
            .build()
            .also {
                NotificationManagerCompat.from(this).notify(PERMISSION_NOTIFICATION_ID, it)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerChannel() {
        val name = getString(R.string.channel_name)

        val channel = NotificationChannel(CHANNEL_ID, name, IMPORTANCE_LOW).apply {
            description = getString(R.string.channel_description)
        }

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).run {
            createNotificationChannel(channel)
        }
    }

    private fun hideForegroundNotification() {
        stopForeground(true)
        NotificationManagerCompat.from(this).cancel(FOREGROUND_NOTIFICATION_ID)
    }

    override fun onDestroy() {
        super.onDestroy()
        hideForegroundNotification()
    }
}

class TurnOffBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var observeSettings: ObserveSettings

    @Inject
    lateinit var saveSettings: SaveSettings

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.hidePermissionNotification()

        (context?.applicationContext as? ComponentApp)?.component?.inject(this)

        GlobalScope.launch {
            val settings = observeSettings.invoke().first()
            saveSettings(settings.copy(isBackgroundServiceEnabled = false))
        }
    }
}

private fun Context.hidePermissionNotification() {
    NotificationManagerCompat.from(this).cancel(PERMISSION_NOTIFICATION_ID)
}

private fun Context.isPermissionGranted() =
    checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
