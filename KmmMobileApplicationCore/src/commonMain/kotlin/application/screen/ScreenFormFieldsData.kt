package com.purenative.application.screen

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMScreenFormFieldsData", "ScreenFormFieldsData", true)
data class ScreenFormFieldsData<FIELD>(
    private val data: Map<FIELD, String> = emptyMap()
) {
    fun get(field: FIELD): String = this.data[field] ?: ""

    fun updating(data: String, field: FIELD): ScreenFormFieldsData<FIELD> =
        ScreenFormFieldsData(this.data + mapOf(field to data))

    fun clearing(field: FIELD): ScreenFormFieldsData<FIELD> =
        ScreenFormFieldsData(this.data + mapOf(field to ""))

    override fun toString(): String = this.data.toString()
}