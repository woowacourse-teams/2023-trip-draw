import com.teamtripdraw.android.support.framework.presentation.log.tripDrawLogUtil.CrashlyticsHandler

class CrashlyticsHandlerImpl : CrashlyticsHandler {
    override fun recordException(throwable: Throwable, keyName: String, keyValue: String) {
        // no_op
    }
}
