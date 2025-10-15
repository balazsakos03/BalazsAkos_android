package com.example.progresshabitplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // [cite: 63]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, // [cite: 65]
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment // [cite: 69]
        return inflater.inflate(R.layout.fragment_profile, container, // [cite: 70]
            false) // [cite: 68]
    }
}