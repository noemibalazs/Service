package com.noemi.service.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.noemi.service.R
import com.noemi.service.base.BaseFragment
import com.noemi.service.databinding.FragmentAlarmManagerBinding
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class FragmentAlarmManager : BaseFragment<FragmentAlarmManagerBinding>(R.layout.fragment_alarm_manager) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAlarmManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = requireContext().getString(R.string.label_alarm_manager)

        binding.alarmCardView.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                binding.alarmTimePicker.hour,
                binding.alarmTimePicker.minute,
                0
            )

            setUpDailyAlarm(calendar.timeInMillis)
            navigateToSource()
        }
    }

    private fun setUpDailyAlarm(time: Long) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.action = USER_ALARM_ACTION
        val pendingIntent =
            PendingIntent.getBroadcast(requireContext(), 12, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)

        val orderingHour =
            String.format(getString(R.string.label_your_daily_alarm_toast, getOrderingHour(time)))

        Toast.makeText(requireContext(), orderingHour, Toast.LENGTH_LONG).show()
    }

    private fun getOrderingHour(time: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(time))
    }

    companion object {
        const val USER_ALARM_ACTION = "User custom alarm action."
    }
}