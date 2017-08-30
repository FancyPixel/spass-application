package it.fancypixel.spassapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

import com.samsung.android.sdk.SsdkUnsupportedException
import com.samsung.android.sdk.pass.Spass
import com.samsung.android.sdk.pass.SpassFingerprint
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;





class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fabric.with(this, Crashlytics())
        val messageView: TextView = findViewById(R.id.message) as TextView

        val mSpass = Spass()
        try {
            mSpass.initialize(this)
            val mSpassFingerprint = SpassFingerprint(this)
            if (mSpass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT)) {
                mSpassFingerprint.startIdentifyWithDialog(this, object : SpassFingerprint.IdentifyListener {
                    override fun onFinished(i: Int) {
                        if (i == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS) {
                            messageView.setText("Authentication Success")
                        } else if (i == SpassFingerprint.STATUS_USER_CANCELLED || i == SpassFingerprint.STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE) {
                            mSpassFingerprint.cancelIdentify()
                            messageView.setText("Authentication Canceled")
                        } else if (i == SpassFingerprint.STATUS_TIMEOUT_FAILED) {
                            mSpassFingerprint.cancelIdentify()
                            messageView.setText("Authentication Timeout Failed")
                        }
                    }

                    override fun onReady() {

                    }

                    override fun onStarted() {

                    }

                    override fun onCompleted() {

                    }
                }, false)
            } else {
                Toast.makeText(this, "Enable Fingerprint", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SsdkUnsupportedException) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }
}
