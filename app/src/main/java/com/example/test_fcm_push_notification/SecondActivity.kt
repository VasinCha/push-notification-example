package com.example.test_fcm_push_notification

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.test_fcm_push_notification.ui.theme.TestfcmpushnotificationTheme

class SecondActivity : ComponentActivity() {
    var TAG = "SecondActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")
        val key1 = intent.getStringExtra("key_1")
        val key2 = intent.getStringExtra("key_2")


        Log.i(TAG, "@@@@ second intent title: $title")
        Log.i(TAG, "@@@@ second intent body: $body")
        Log.i(TAG, "@@@@ second intent key_1: $key1")
        Log.i(TAG, "@@@@ second intent key_2: $key2")

        setContent {
            TestfcmpushnotificationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("This is Second Screen \nNotification Title: $title\nNotification Body: $body\nData Key 1: $key1\n" +
                            "Data Key 2: $key2")
                }
            }
        }
    }
}