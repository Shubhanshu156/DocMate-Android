package com.example.docmate.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.docmate.MainActivity
import com.example.docmate.R
import com.example.docmate.Utility.datastore.StoredToken
import com.example.docmate.data.Repositories.HomeRepositry
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseNotificationService() : FirebaseMessagingService() {
    @Inject
    lateinit var homerepo: HomeRepositry

    @Inject
    lateinit var StoredToken: StoredToken

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("FirebaseNotification", "onMessageReceived:${remoteMessage.data} ",)
        val channelId = "docmate_channelid"
        val notificationId = 0
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val senders_name = remoteMessage.data["SendersName"]
        val meet_time = remoteMessage.data["MeetTime"]
        val image_url = remoteMessage.data["image"]
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(message)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.stethoscope_solid)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setStyle(bigTextStyle)


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val imageUrl = image_url
        if (imageUrl != null) {
            Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        // Set the downloaded image as the notification icon
                        notificationBuilder.setLargeIcon(resource)
                        notificationManager.notify(notificationId, notificationBuilder.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle if the image loading is cleared or fails
                    }
                })
        } else {

            notificationManager.notify(notificationId, notificationBuilder.build())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                title,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Log.e("Updated Token", "onNewToken: $token")
        val coroutineScope = CoroutineScope(Dispatchers.Default)


        coroutineScope.launch {
            try {
                if (isSignedIn()) {
                    homerepo.sendtoken(token)
                }
            } catch (e: Exception) {
                Log.e("FCM token", "onNewToken: User Not Signed")
            }
        }
    }

    suspend fun isSignedIn(): Boolean {
    var isUserSignedIn = false

    StoredToken.getToken().collect { token->
        isUserSignedIn = !token.isNullOrEmpty()
    }

    return isUserSignedIn // Return the sign-in status outside the lambda or coroutine block
}

}