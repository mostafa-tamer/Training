package com.example.training

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.training.databinding.FragmentExerciseBinding

class ExerciseFragment : Fragment() {

    private val viewModel: ExerciseViewModel by viewModels()
    private lateinit var binding: FragmentExerciseBinding
    private lateinit var args: ExerciseFragmentArgs

    private var setsNumber = 0L
    private var setsBreak = 0L
    private var countsNumber = 0L
    private var countDuration = 0L
    private var countsBreak = 0L

    private lateinit var mediaPlayer: MediaPlayer

    private var goOnce = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentExerciseBinding.inflate(layoutInflater)
        args = ExerciseFragmentArgs.fromBundle(requireArguments())
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sound)

        binding.setsNumber.text = "Sets Number: " + args.setsNumber.toString()
        binding.setsBreak.text = "Break Between Sets: " + args.setsBreak.toString()
        binding.countsNumber.text = "Counts Number: " + args.countsNumber.toString()
        binding.countDuration.text = "Count Duration: " + args.countDuration.toString()
        binding.countsBreak.text = "Break Between Counts: " + args.countsBreak.toString()


        if (goOnce) {
            goOnce = false
            keepScreenAwake()
            setAttr()
            setExerciseViewModelAttr()
            run()
        }

        return binding.root
    }

    private fun keepScreenAwake() {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun setExerciseViewModelAttr() {
        viewModel.setSetsNumber(args.setsNumber)
        viewModel.setSetsBreak(args.setsBreak)
        viewModel.setCountsNumber(args.countsNumber)
        viewModel.setCountDuration(args.countDuration)
        viewModel.setCountsBreak(args.countsBreak)
    }

    private fun setAttr() {
        setsNumber = args.setsNumber
        setsBreak = args.setsBreak
        countsNumber = args.countsNumber
        countDuration = args.countDuration
        countsBreak = args.countsBreak
    }

    private fun run() {
        binding.counter.text = countDuration.toString()
        viewModel.startCountActiveTimer(countDuration)
        playSound()
        resetListener()

        setsObserver()
        setsBreakObserver()
        finishedSetBreakObserver()
        countsObserver()
        activeObserver()
        finishedActiveObserver()
        breakObserver()
        finishedBreakObserver()
    }

    private fun setsBreakObserver() {
        viewModel.getSetBreakTimer().observe(viewLifecycleOwner) {
            binding.counter.text = it.toString()
        }
    }

    private fun finishedSetBreakObserver() {
        viewModel.getFinishedCounterSetTimer().observe(viewLifecycleOwner) {
            playSound()
            if (viewModel.getSetsNumber() > 1) {
                viewModel.startCountActiveTimer(args.countDuration)
                viewModel.setSetsNumber(viewModel.getSetsNumber().minus(1))
                viewModel.setCountsNumber(args.countsNumber)
            }
        }
    }

    private fun setsObserver() {
        viewModel.getSetsNumberObservable().observe(viewLifecycleOwner) {
            binding.remainingSets.text = "Remaining Sets: " + it.toString()
        }
    }

    private fun finishedBreakObserver() {
        viewModel.getFinishedBreakCounter().observe(viewLifecycleOwner) {
            if (viewModel.getCountsNumber() > 1) {
                playSound()
                viewModel.setCountsNumber(viewModel.getCountsNumber().minus(1))
                viewModel.startCountActiveTimer(countDuration)
            }
        }
    }

    private fun breakObserver() {
        viewModel.getCounterBreakTimer().observe(viewLifecycleOwner) {
            binding.counter.text = it.toString()
        }
    }

    private fun activeObserver() {
        viewModel.getCounterActiveTimer().observe(viewLifecycleOwner) {
            activeUI()
            binding.counter.text = it.toString()
        }
    }

    private fun activeUI() {
        binding.root.setBackgroundColor(Color.parseColor("#aaaaaa"))
        binding.state.text = "Active!"
    }

    private fun countsObserver() {
        viewModel.getCountsNumberObservable().observe(viewLifecycleOwner) {
            binding.remainingCounts.text = "Remaining Counts: $it"
        }
    }

    private fun finishedActiveObserver() {
        viewModel.getFinishedActiveCounter().observe(viewLifecycleOwner) {
            playSound()
            if (viewModel.getCountsNumber() > 1) {

                binding.root.setBackgroundColor(Color.rgb(255, 192, 203))
                binding.state.text = "Break!"
                viewModel.startCountBreakTimer(countsBreak)
                viewModel.setCountsBreak(args.setsBreak)
            } else {
                if (viewModel.getSetsNumber() > 1) {
                    binding.root.setBackgroundColor(Color.rgb(255, 192, 203))
                    binding.state.text = "Set Break!"
                    viewModel.startCountBreakTimer(countsBreak)
                    viewModel.startSetBreakTimer(args.setsBreak)
                } else {
                    viewModel.stopCounters()
                    doneUI()
                }
            }
        }
    }

    private fun doneUI() {
        binding.root.setBackgroundColor(Color.rgb(144, 238, 144))
        binding.state.text = "Done!"
        binding.counter.visibility = View.GONE
        binding.remainingSets.visibility = View.GONE
        binding.remainingCounts.visibility = View.GONE
    }


    private fun resetListener() {
        binding.reset.setOnClickListener {
            playSound()

            binding.counter.visibility = View.VISIBLE
            binding.remainingSets.visibility = View.VISIBLE
            binding.remainingCounts.visibility = View.VISIBLE
            resetSet()
        }
    }

    private fun resetSet() {
        viewModel.setSetsNumber(args.setsNumber)
        viewModel.setSetsBreak(args.setsBreak)
        viewModel.setCountsNumber(args.countsNumber)
        viewModel.setCountDuration(args.countDuration)
        viewModel.setCountsBreak(args.countsBreak)
        viewModel.startCountActiveTimer(countDuration)
    }

    private fun playSound() {
        mediaPlayer.stop()
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}