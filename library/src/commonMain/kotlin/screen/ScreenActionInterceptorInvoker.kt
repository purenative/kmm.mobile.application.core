package screen

// Обеспечивает согласованность обработки действий внутри экрана приложения
expect class ScreenActionInterceptorInvoker() {
    fun invoke(action: suspend () -> Unit)
    fun destroy()
}