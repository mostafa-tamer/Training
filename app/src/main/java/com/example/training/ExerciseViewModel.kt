package com.example.training

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExerciseViewModel : ViewModel() {

    private var setsNumber = MutableLiveData<Long>()
    fun setSetsNumber(value: Long) {
        setsNumber.value = value
    }

    fun getSetsNumber() = setsNumber.value!!
    fun getSetsNumberObservable() = setsNumber

    private var setsBreak = MutableLiveData<Long>()
    fun setSetsBreak(value: Long) {
        setsBreak.value = value
    }

    fun getSetsBreak() = setsBreak.value!!

    private var countsNumber = MutableLiveData<Long>()
    fun setCountsNumber(value: Long) {
        countsNumber.value = value
    }

    fun getCountsNumber() = countsNumber.value!!
    fun getCountsNumberObservable() = countsNumber

    private var countDuration = MutableLiveData<Long>()
    fun setCountDuration(value: Long) {
        countDuration.value = value
    }

    fun getCountDuration() = countDuration.value!!


    private var countsBreak = MutableLiveData<Long>()
    fun setCountsBreak(value: Long) {
        countsBreak.value = value
    }

    fun getCountsBreak() = countsBreak.value!!

    private lateinit var setBreakTimer: CountDownTimer
    private lateinit var countActiveTimer: CountDownTimer
    private lateinit var countBreakTimer: CountDownTimer

    private val counterSetBreakTimer = MutableLiveData<Long>()
    fun getSetBreakTimer() = counterSetBreakTimer

    private val finishedCounterSetBreakTimer = MutableLiveData<Boolean>()
    fun getFinishedCounterSetTimer() = finishedCounterSetBreakTimer

    private val counterActiveTimer = MutableLiveData<Long>()
    fun getCounterActiveTimer() = counterActiveTimer

    private val finishedActiveCounter = MutableLiveData<Boolean>()
    fun getFinishedActiveCounter() = finishedActiveCounter

    private val counterBreakTimer = MutableLiveData<Long>()
    fun getCounterBreakTimer() = counterBreakTimer

    private val finishedBreakCounter = MutableLiveData<Boolean>()
    fun getFinishedBreakCounter() = finishedBreakCounter

    fun startSetBreakTimer(duration: Long) {
        stopCounters()

        setBreakTimer = object : CountDownTimer(duration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counterSetBreakTimer.value = Math.round(millisUntilFinished.toDouble() / 1000)
            }

            override fun onFinish() {
                finishedCounterSetBreakTimer.value = true
            }

        }.start()
    }

    fun startCountActiveTimer(duration: Long) {
        stopCounters()

        countActiveTimer = object : CountDownTimer(duration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counterActiveTimer.value = Math.round(millisUntilFinished.toDouble() / 1000)
            }

            override fun onFinish() {
                finishedActiveCounter.value = true
            }

        }.start()
    }

    fun startCountBreakTimer(duration: Long) {
        stopCounters()
        countBreakTimer = object : CountDownTimer(duration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counterBreakTimer.value = Math.round(millisUntilFinished.toDouble() / 1000)
            }

            override fun onFinish() {
                finishedBreakCounter.value = true
            }

        }.start()
    }

    fun stopCounters() {
        if (::countActiveTimer.isInitialized) {
            countActiveTimer.cancel()
        }
        if (::countBreakTimer.isInitialized) {
            countBreakTimer.cancel()
        }
        if (::setBreakTimer.isInitialized) {
            setBreakTimer.cancel()
        }
    }
}

