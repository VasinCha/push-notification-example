package com.example.test_fcm_push_notification

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.Manifest
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.test_fcm_push_notification.ui.theme.TestfcmpushnotificationTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private lateinit var firebaseMessaging: FirebaseMessaging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestfcmpushnotificationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("This is Main Screen")
                }
            }
        }

        askNotificationPermission()
        handleIntent(intent)
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.i(TAG, "@@@@ onNewIntent")
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        // This function is invoked when a notification in the notification bar is clicked.
        // In this project, push notifications can be created in two ways:
        // 1. By the default FCM handler when the application is not running.
        // 2. By the onMessageReceived() method in FCMService.kt when the application is running in the foreground or background.
        // Therefore, you may notice differences in the notification UI between the default FCM notifications (when the app is not running)
        // and custom notifications (created via the onMessageReceived() method when the app is running).
        //
        // Example Payload from FCM Server
        // {
        //      "to": "device_token",
        //      "data": {
        //          "target_page": "second_page",
        //          "key_1": "This is value Key 1",
        //          "key_2": "This is value Key 2"
        //      },
        //      "notification": {
        //          "title": "Notification Title",
        //          "body": "Notification Body"
        //      }
        // }

        Log.i(TAG, "@@@@ handleIntent This called when click from Notification Bar")
        intent?.extras?.let {
            val notificationTitle = it.getString("title")
            val notificationBody = it.getString("body")
            val dataPage = it.getString("target_page")
            val dataKey1 = it.getString("key_1")
            var dataKey2 = it.getString("key_2")

            Log.i(TAG, "@@@@ intent title: $notificationTitle")
            Log.i(TAG, "@@@@ intent body: $notificationBody")
            Log.i(TAG, "@@@@ intent target_page: $dataPage")
            Log.i(TAG, "@@@@ intent key_1: $dataKey1")
            Log.i(TAG, "@@@@ intent key_2: $dataKey2")

            /*if (dataPage == "second_page") {
                startActivity(Intent(this, SecondActivity::class.java).apply {
                    // you can add values(if any) to pass to the next class or avoid using `.apply`
                    putExtra("title", notificationTitle)
                    putExtra("body", notificationBody)
                    putExtra("key_1", dataKey1)
                    putExtra("key_2", dataKey2)
                })
            } else if (dataPage == "third_page") {
                startActivity(Intent(this, ThirdActivity::class.java).apply {
                    // you can add values(if any) to pass to the next class or avoid using `.apply`
                    putExtra("title", notificationTitle)
                    putExtra("body", notificationBody)
                    putExtra("key_1", dataKey1)
                    putExtra("key_2", dataKey2)
                })
            }*/
            when (dataPage) {
                "second_page" -> {
                    startActivity(Intent(this, SecondActivity::class.java).apply {
                        // you can add values(if any) to pass to the next class or avoid using `.apply`
                        putExtra("title", notificationTitle)
                        putExtra("body", notificationBody)
                        putExtra("key_1", dataKey1)
                        putExtra("key_2", dataKey2)
                    })
                }
                "third_page" -> {
                    startActivity(Intent(this, ThirdActivity::class.java).apply {
                        // you can add values(if any) to pass to the next class or avoid using `.apply`
                        putExtra("title", notificationTitle)
                        putExtra("body", notificationBody)
                        putExtra("key_1", dataKey1)
                        putExtra("key_2", dataKey2)
                    })
                }
            }
        }
    }

    private fun registerFMCToken() {
        firebaseMessaging = FirebaseMessaging.getInstance()
        firebaseMessaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(TAG, "@@@@ Token: $token")
                // Send token to server
            } else {
                Log.w(TAG, "@@@@ Failed to get token")
            }
        }
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            registerFMCToken()
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestfcmpushnotificationTheme {
        Greeting("Android")
    }
}