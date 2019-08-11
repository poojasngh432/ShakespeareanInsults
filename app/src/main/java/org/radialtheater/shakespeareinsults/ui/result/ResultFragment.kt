package org.radialtheater.shakespeareinsults.ui.result

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import org.radialtheater.shakespeareinsults.R
import org.radialtheater.shakespeareinsults.databinding.FragmentResultBinding
import org.radialtheater.shakespeareinsults.shared.SharedViewModel
import org.radialtheater.shakespeareinsults.utilities.ShareHelper
import org.radialtheater.shakespeareinsults.utilities.SpeechHelper

class ResultFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel
    private lateinit var speechHelper: SpeechHelper
    private lateinit var shareHelper: ShareHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a reference to the shared view model
        viewModel = ViewModelProviders.of(requireActivity())
            .get(SharedViewModel::class.java)

        speechHelper = SpeechHelper(requireActivity())
        shareHelper = ShareHelper()
    }

    // Check for a non-empty or null insult value
    override fun onResume() {
        super.onResume()
        if (viewModel.insult.value.isNullOrEmpty()) {
            navigateUp()
        } else {
            requireActivity().title = getString(R.string.title_result_fragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Set up binding
        val binding = FragmentResultBinding.inflate(
            inflater, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //  Set up toolbar
        val toolbar = binding.root.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)

        return binding.root
    }

    // Add the share button
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_result_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // Handle options menu item selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> navigateUp()
            R.id.action_copy -> shareHelper
                .copyToClipboard(requireActivity(), viewModel)
            R.id.action_share_text -> shareHelper
                .shareInsultText(requireActivity(), viewModel)
            R.id.action_share_file -> speechHelper
                .createShareFile(shareHelper, viewModel)
            R.id.action_speak -> speakInsult()
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Return to previous navigation destination
    private fun navigateUp(): Boolean {
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        navController.navigateUp()
        return true
    }

    // Say it loud
    private fun speakInsult(): Boolean {
        speechHelper.say(viewModel.insultForSharing ?: return false)
        return true
    }

}
