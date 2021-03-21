package io.github.gianpamx.shscale.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import io.github.gianpamx.shscale.MainActivity
import io.github.gianpamx.shscale.R
import io.github.gianpamx.shscale.app.ComponentApp
import io.github.gianpamx.shscale.background.BackgroundEvent.ACTIVITY_STARTED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

const val BG_SERVICE_EVENT = "BG_SERVICE_EVENT"

private const val NOTIFICATION_ID = 52265
private const val CHANNEL_ID = "BACKGROUND_CHANNEL"

enum class BackgroundEvent {
    ACTIVITY_STARTED,
    ACTIVITY_STOPPED
}

class BackgroundService : LifecycleService() {
    @Inject
    lateinit var backgroundModel: BackgroundModel

    private var runInTheBackground: Boolean? = null

    override fun onCreate() {
        super.onCreate()

        (application as ComponentApp).component.inject(this)

        backgroundModel
            .settings
            .onEach {
                runInTheBackground = it.runInTheBackground
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
                hideNotification()
                START_NOT_STICKY
            }
            runInTheBackground == false -> {
                hideNotification()
                stopSelf(startId)
                START_NOT_STICKY
            }
            else -> {
                goToBackground()
                START_STICKY
            }
        }
    }

    private fun goToBackground() {
        val pendingIntent = Intent(this, MainActivity::class.java).let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) registerChannel()

        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_weight_scale)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setContentTitle(getString(R.string.channel_name))
            .build()
            .also {
                NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, it)
                startForeground(NOTIFICATION_ID, it)
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

    private fun hideNotification() {
        stopForeground(true)
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
    }

    override fun onDestroy() {
        super.onDestroy()
        hideNotification()
    }
}
