package alert

import base.BaseProxy

enum class AlertActionStyle {
    NORMAL, CANCEL, DESTRUCTIVE
}

data class AlertActionConfiguration(
    val id: String,
    val title: String,
    val style: AlertActionStyle = AlertActionStyle.NORMAL
)

enum class AlertStyle {
    NORMAL, SHEET
}

interface AlertActionDelegate {
    fun handleAlertAction(actionId: String)
}

data class AlertConfiguration(
    val title: String?,
    val message: String? = null,
    val style: AlertStyle = AlertStyle.NORMAL,
    val actionDelegate: AlertActionDelegate,
    val actions: List<AlertActionConfiguration>
)

interface AlertPresenterHandler {
    fun presentAlert(configuration: AlertConfiguration)
}

object AlertPresenterProxy: BaseProxy<AlertPresenterHandler>() {
    fun presentAlert(configuration: AlertConfiguration) {
        proxy { it.presentAlert(configuration) }
    }
}