package com.arbo.oracoes.presentation.mood.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.Text
import com.arbo.oracoes.domain.model.TextCategory
import com.arbo.oracoes.domain.usecase.TextFindByIdsUseCase
import com.arbo.oracoes.presentation.base.view.BaseFragment
import com.arbo.oracoes.presentation.mood.viewmodel.TextListViewModel
import com.arbo.oracoes.presentation.mood.viewmodel.TextListViewModelFactory
import com.arbo.oracoes.presentation.util.extension.hide
import com.arbo.oracoes.presentation.util.extension.show
import kotlinx.android.synthetic.main.fragment_text_list.*
import org.kodein.di.generic.instance

class TextListFragment : BaseFragment() {

    private val textFindByIdsUseCase: TextFindByIdsUseCase by instance()

    private val textListViewModel: TextListViewModel by viewModels {
        TextListViewModelFactory(textFindByIdsUseCase)
    }

    private val textListAdapter =
        TextListAdapter(textClickListener = object : TextClickListener {
            override fun onClick(text: Text) {
                val bundle = Bundle()
                bundle.putSerializable(TextFragment.ARG_TEXT, text)
                findNavController().navigate(R.id.to_text_fragment, bundle)
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_text_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupObservers()
        setupRecycler()
        loadTextList()
    }

    private fun loadTextList() {
        val textCategory = arguments?.getSerializable(ARG_TEXT_CATEGORY) as TextCategory?
        textCategory?.let {
            bind(it)
        }
    }

    private fun bind(textCategory: TextCategory) {
        textListViewModel.loadAudios(textCategory.texts)
        text_title?.text = textCategory.title
    }

    private fun setupRecycler() {
        text_list_recycler?.adapter = textListAdapter
    }

    private fun setupObservers() {
        textListViewModel.texts.observe(viewLifecycleOwner, Observer {
            textListAdapter.update(it)
        })
        textListViewModel.validationMessage.observe(viewLifecycleOwner, Observer {
            displayValidationMessage(it)
        })
        textListViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                text_list_progress.show()
                text_list_recycler.hide()
            } else {
                text_list_progress.hide()
                text_list_recycler.show()
            }
        })
    }

    companion object {
        const val ARG_TEXT_CATEGORY = "ARG_TEXT_CATEGORY"
    }
}