package uz.gita.dima.waziypalar.view.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.databinding.FragmentAuthBinding
import uz.gita.dima.waziypalar.utils.AuthManager
import uz.gita.dima.waziypalar.utils.UIHelper.showSnack
import uz.gita.dima.waziypalar.utils.viewLifecycleLazy

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewLifecycleLazy { FragmentAuthBinding.bind(requireView()) }
    private lateinit var authManager: AuthManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authManager = AuthManager(requireActivity())
        setListeners()
    }

    private fun setListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finish()
        }

        binding.btnSignUp.setOnClickListener {
            if (!authManager.isUserLoggedIn) authManager.authUser()
            else showSnack(requireView(), "Already signedIn")
        }
    }
}