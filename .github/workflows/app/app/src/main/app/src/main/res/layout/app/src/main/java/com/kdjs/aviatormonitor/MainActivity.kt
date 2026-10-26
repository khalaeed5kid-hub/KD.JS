package com.kdjs.aviatormonitor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvRoundsCount: TextView
    private lateinit var tvLastMultiplier: TextView
    private lateinit var tvLog: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnExport: Button
    private lateinit var btnClear: Button

    private var isMonitoring = false
    private var isWaitingForStartPoint = false
    private var roundsCount = 0
    private var lastMultiplier = "--"
    private val logBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ربط العناصر واجهات المستخدم
        tvStatus = findViewById(R.id.tvStatus)
        tvRoundsCount = findViewById(R.id.tvRoundsCount)
        tvLastMultiplier = findViewById(R.id.tvLastMultiplier)
        tvLog = findViewById(R.id.tvLog)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        btnExport = findViewById(R.id.btnExport)
        btnClear = findViewById(R.id.btnClear)

        updateUIState("متوقف")

        // زر بدء المراقبة
        btnStart.setOnClickListener {
            isMonitoring = true
            isWaitingForStartPoint = true
            updateUIState("انتظار نقطة البداية 1.00x...")
            appendLog("تم بدء المراقبة. جاري البحث عن نقطة الانطلاق 1.00x...")
        }

        // زر إيقاف المراقبة
        btnStop.setOnClickListener {
            isMonitoring = false
            isWaitingForStartPoint = false
            updateUIState("متوقف")
            appendLog("تم إيقاف المراقبة يدوياً. البيانات محفوظة.")
        }

        // زر حذف السجل
        btnClear.setOnClickListener {
            roundsCount = 0
            lastMultiplier = "--"
            logBuilder.clear()
            tvRoundsCount.text = "الجولات: 0"
            tvLastMultiplier.text = "آخر معامل: --"
            tvLog.text = "تم مسح السجل."
        }

        // زر تصدير ومشاركة السجل
        btnExport.setOnClickListener {
            val shareText = logBuilder.toString()
            if (shareText.isEmpty()) {
                appendLog("السجل فارغ للمشاركة.")
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "سجل نتائج Aviator")
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(intent, "مشاركة السجل عبر"))
        }
    }

    private fun updateUIState(status: String) {
        tvStatus.text = "الحالة: $status"
    }

    private fun appendLog(message: String) {
        val timeStamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        logBuilder.append("[$timeStamp] $message\n")
        tvLog.text = logBuilder.toString()
    }
}

