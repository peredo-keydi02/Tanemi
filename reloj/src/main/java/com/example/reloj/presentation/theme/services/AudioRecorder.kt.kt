package com.example.reloj.presentation.theme.services

import android.media.MediaRecorder
import android.os.Environment

class AudioRecorder {
    private var recorder: MediaRecorder? = null
    private val outputFile = "${Environment.getExternalStorageDirectory().absolutePath}/recorded_audio.3gp"

    fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(outputFile)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            prepare()
            start()
        }
    }

    fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }
}
