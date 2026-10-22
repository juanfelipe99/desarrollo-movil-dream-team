package com.equipo2.vinilos.ui.common

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.equipo2.vinilos.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/** Abre el selector de fecha de Material sin días futuros, con la fecha elegida o la de hoy marcada. */
fun Fragment.showPastDatePicker(tag: String, @StringRes title: Int, selected: LocalDate?, onPicked: (LocalDate) -> Unit) {
    // Un segundo toque mientras se abre no abre otro selector
    if (childFragmentManager.findFragmentByTag(tag) != null) return

    val today = LocalDate.now().toUtcMillis()
    val constraints = CalendarConstraints.Builder()
        .setEnd(today)
        .setValidator(DateValidatorPointBackward.before(today))
        .build()

    val picker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(title)
        .setSelection(selected?.toUtcMillis() ?: today)
        .setCalendarConstraints(constraints)
        .setPositiveButtonText(R.string.action_accept)
        .setNegativeButtonText(R.string.action_cancel)
        .build()

    picker.addOnPositiveButtonClickListener { onPicked(it.toLocalDate()) }
    picker.show(childFragmentManager, tag)
}

/** Al girar la pantalla el selector abierto se recrea sin su acción, y esto se la vuelve a dar. */
fun Fragment.reconnectDatePicker(tag: String, onPicked: (LocalDate) -> Unit) {
    @Suppress("UNCHECKED_CAST")
    val picker = childFragmentManager.findFragmentByTag(tag) as? MaterialDatePicker<Long> ?: return
    picker.addOnPositiveButtonClickListener { onPicked(it.toLocalDate()) }
}

// El selector de Material trabaja con la medianoche de cada día en UTC
private fun LocalDate.toUtcMillis(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.toLocalDate(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
