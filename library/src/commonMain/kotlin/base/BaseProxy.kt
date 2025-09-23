package base

abstract class BaseProxy<HANDLER> {
    private var handler: HANDLER? = null

    fun setHandler(handler: HANDLER) {
        this.handler = handler
    }

    fun proxy(action: (HANDLER) -> Unit) {
        handler?.let(action)
    }
}