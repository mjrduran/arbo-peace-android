package com.arbo.oracoes.presentation.mood.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.TextCategory
import com.arbo.oracoes.domain.usecase.TextCategoryFindAllUseCase
import com.arbo.oracoes.presentation.base.view.BaseFragment
import com.arbo.oracoes.presentation.mood.viewmodel.MoodViewModel
import com.arbo.oracoes.presentation.mood.viewmodel.MoodViewModelFactory
import com.arbo.oracoes.presentation.util.extension.hide
import com.arbo.oracoes.presentation.util.extension.show
import kotlinx.android.synthetic.main.fragment_mood.*
import org.kodein.di.generic.instance

class MoodFragment : BaseFragment() {

    private val categoryFindAllUseCase: TextCategoryFindAllUseCase by instance()

    private val moodAdapter =
        MoodAdapter(moodClickListener = object : MoodClickListener {
            override fun onClick(textCategory: TextCategory) {
                val bundle = Bundle()
                bundle.putSerializable(TextListFragment.ARG_TEXT_CATEGORY, textCategory)
                findNavController().navigate(R.id.to_text_list, bundle)
            }
        })

    private val moodViewModel: MoodViewModel by viewModels {
        MoodViewModelFactory(categoryFindAllUseCase)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mood, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupObservers()
        setupRecycler()
        moodViewModel.loadCategories()
    }

    private fun setupRecycler() {
        mood_recycler?.adapter = moodAdapter
    }

    private fun setupObservers() {
        moodViewModel.categories.observe(viewLifecycleOwner, Observer {
            moodAdapter.update(it)
        })
        moodViewModel.validationMessage.observe(viewLifecycleOwner, Observer {
            displayValidationMessage(it)
        })
        moodViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                mood_progress.show()
                mood_recycler.hide()
            } else {
                mood_progress.hide()
                mood_recycler.show()
            }
        })
    }
}