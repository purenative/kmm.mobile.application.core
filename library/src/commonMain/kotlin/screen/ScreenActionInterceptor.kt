package screen

// Обработчик действий пользователя и жизненного цикла экрана приложения
open class ScreenActionInterceptor<ROUTE: ScreenRoute, STATE: ScreenState>(
    val route: ROUTE,
    val initialState: STATE
) {
    private val invoker = ScreenActionInterceptorInvoker()
    private val stateHolder = ScreenStateHolder<STATE>(initialState)

    val currentState: STATE
        get() = stateHolder.currentState
    var onStateUpdated: ((STATE) -> Unit)? = null

    init {
        stateHolder.onStateUpdated = { newState ->
            onStateUpdated?.let { it(newState) }
        }
    }

    open fun onCreate() { }
    open fun onAppear() { }
    open fun onDisappear() { }
    fun onDestroy() {
        invoker.destroy()
    }

    fun invoke(action: suspend (stateHolder: ScreenStateHolder<STATE>) -> Unit) {
        invoker.invoke {
            action(stateHolder)
        }
    }
}