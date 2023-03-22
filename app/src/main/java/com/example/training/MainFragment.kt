package com.example.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.training.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(layoutInflater)

        binding.button.setOnClickListener {

            var numberOfSets = binding.numberOfSets.text.toString()
            var setsBreak = binding.setsBreak.text.toString()
            var countDuration = binding.countDuration.text.toString()
            var countsInEachSet = binding.countsInEachSet.text.toString()
            var countsBreak = binding.countsBreak.text.toString()

            if (numberOfSets.isEmpty() || numberOfSets == "0") numberOfSets = "1"
            if (setsBreak.isEmpty()) setsBreak = "0"
            if (countDuration.isEmpty() || countDuration == "0") countDuration = "1"
            if (countsInEachSet.isEmpty() || countsInEachSet == "0") countsInEachSet = "1"
            if (countsBreak.isEmpty()) countsBreak = "0"


            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToActivityFragment(
                    numberOfSets.toLong(),
                    setsBreak.toLong(),
                    countsInEachSet.toLong(),
                    countDuration.toLong(),
                    countsBreak.toLong()
                )
            )
        }
        return binding.root
    }

}
