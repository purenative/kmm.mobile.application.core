package screen

import base.BaseProxy

// Маршрут к экрану приложения
interface ScreenRoute { }

// Абстракция над платформенным механизмом навигации.
// Реализуется в нативной части.
interface ScreenPresenterHandler {
    fun presentScreen(route: ScreenRoute)
}

object ScreenPresenterProxy: BaseProxy<ScreenPresenterHandler>() {
    fun presentScreen(route: ScreenRoute) {
        proxy { it.presentScreen(route) }
    }
}