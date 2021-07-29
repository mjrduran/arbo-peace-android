package com.arbo.oracoes.presentation.more.view

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.ReminderTime
import com.arbo.oracoes.domain.repository.PreferencesRepository
import com.arbo.oracoes.domain.repository.ReminderSchedulerRepository
import com.arbo.oracoes.presentation.base.view.BaseFragment
import com.arbo.oracoes.presentation.more.viewmodel.ReminderViewModel
import com.arbo.oracoes.presentation.more.viewmodel.ReminderViewModelFactory
import kotlinx.android.synthetic.main.fragment_reminder.*
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*


class ReminderFragment : BaseFragment() {

    private val preferencesRepository: PreferencesRepository by instance()
    private val reminderSchedulerRepository: ReminderSchedulerRepository by instance()

    private var timePickerDialog: TimePickerDialog? = null

    private val reminderViewModel: ReminderViewModel by viewModels {
        ReminderViewModelFactory(preferencesRepository, reminderSchedulerRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListeners()
        setupObservers()
        reminderViewModel.loadReminderTime()
    }

    private fun setupObservers() {
        reminderViewModel.validationMessage.observe(viewLifecycleOwner, Observer {
            displayValidationMessage(it)
        })
        reminderViewModel.reminderTime.observe(viewLifecycleOwner, Observer {
            reminder_time.text = formatTime(it)
            createTimePicker(it)
        })
        reminderViewModel.reminderEnabled.observe(viewLifecycleOwner, Observer {
            reminder_switch?.isChecked = it
            reminder_time?.isEnabled = it
        })
    }

    private fun createTimePicker(time: ReminderTime) {
        val activity = activity ?: return
        timePickerDialog = TimePickerDialog(
            activity,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                reminderViewModel.setReminderTime(hour, minute)
            }, time.hour, time.minute, true
        )
    }

    private fun formatTime(it: ReminderTime): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, it.hour)
        calendar.set(Calendar.MINUTE, it.minute)
        val formatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private fun setupListeners() {
        reminder_time?.setOnClickListener {
            timePickerDialog?.show()
        }

        back_button?.setOnClickListener {
            findNavController().popBackStack()
        }

        reminder_switch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                reScheduleReminder()
            } else {
                cancelReminder()
            }
        }
    }

    private fun reScheduleReminder() {
        reminder_time.isEnabled = true
        reminderViewModel.reScheduleReminder()
    }

    private fun cancelReminder() {
        reminder_time.isEnabled = false
        reminderViewModel.cancelReminder()
    }

    companion object {
        private const val TIME_FORMAT = "HH:mm"
    }
}