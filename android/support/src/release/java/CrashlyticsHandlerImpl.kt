import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.teamtripdraw.android.support.framework.presentation.log.tripDrawLogUtil.CrashlyticsHandler

class CrashlyticsHandlerImpl : CrashlyticsHandler {
    override fun recordException(throwable: Throwable, keyName: String, keyValue: String) {
        Firebase.crashlytics.run {
            setCustomKey(keyName, keyValue)
            recordException(throwable)
        }
    }
}
