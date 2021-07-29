package com.arbo.oracoes.presentation.mood.view

import android.content.DialogInterface
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.Text
import com.arbo.oracoes.domain.model.analytics.ShareTextClick
import com.arbo.oracoes.domain.model.analytics.TextDontRateEvent
import com.arbo.oracoes.domain.model.analytics.TextRateEvent
import com.arbo.oracoes.domain.model.analytics.TextRateLaterEvent
import com.arbo.oracoes.domain.repository.AnalyticsRepository
import com.arbo.oracoes.presentation.base.view.BaseFragment
import com.arbo.oracoes.presentation.util.ShareHelper
import com.arbo.oracoes.presentation.util.extension.decreaseFontSize
import com.arbo.oracoes.presentation.util.extension.hide
import com.arbo.oracoes.presentation.util.extension.increaseFontSize
import com.arbo.oracoes.presentation.util.extension.show
import com.vorlonsoft.android.rate.AppRate
import kotlinx.android.synthetic.main.fragment_text.*
import org.kodein.di.generic.instance
import java.util.*


class TextFragment : BaseFragment() {

    private var textToSpeech: TextToSpeech? = null
    private var mostRecentUtteranceID: String? = null
    private val analyticsRepository: AnalyticsRepository by instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_text, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val text = arguments?.getSerializable(ARG_TEXT) as Text?
        text?.let {
            bind(it)
        }
        setupListeners()
    }

    private fun setupListeners() {
        val text = arguments?.getSerializable(ARG_TEXT) as Text?
        text_play?.setOnClickListener {
            text?.let {
                speak(text.body)
            }
        }
        text_stop?.setOnClickListener {
            stopSpeak()
        }
        decrease_font_size?.setOnClickListener {
            text_title.decreaseFontSize()
            text_body.decreaseFontSize()
        }
        increase_font_size?.setOnClickListener {
            text_title.increaseFontSize()
            text_body.increaseFontSize()
        }
        back_button?.setOnClickListener {
            findNavController().popBackStack()
        }
        text_share?.setOnClickListener {
            val subject = text?.title ?: ""
            val url = getString(R.string.text_share_app_link)
            val textToShare =
                subject + "\n\n" + text?.body + getString(R.string.text_share_water_mark, url)
            startActivity(
                ShareHelper.createShareContentIntent(
                    subject = subject,
                    text = textToShare
                )
            )
            analyticsRepository.trackEvent(ShareTextClick(subject))
        }
        text_nested_scroll_view?.viewTreeObserver?.addOnScrollChangedListener {
            text_nested_scroll_view?.let {
                val view = it.getChildAt(it.childCount - 1)

                view?.let {
                    val diff: Int =
                        view.bottom - (text_nested_scroll_view.height + text_nested_scroll_view
                            .scrollY)

                    if (diff < 0) {
                        displayRateDialog()
                    }
                }
            }
        }
    }

    private fun bind(text: Text) {
        text_title?.text = text.title
        text_body?.text = text.body
    }

    override fun onResume() {
        super.onResume()
        initTextToSpeech()
    }

    private fun initTextToSpeech() {
        val activity = activity ?: return
        textToSpeech = TextToSpeech(activity, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale("pt", "BR")
                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onDone(utteranceId: String?) {
                        activity.runOnUiThread {
                            text_play?.show()
                            text_stop?.hide()
                            displayRateDialog()
                        }
                    }

                    override fun onError(utteranceId: String?) {

                    }

                    override fun onStart(utteranceId: String?) {

                    }
                })
            }
        })
    }

    private fun displayRateDialog() {
        val activity = activity ?: return
        AppRate.with(activity).setOnClickButtonListener { which ->
            when (which.toInt()) {
                DialogInterface.BUTTON_POSITIVE -> {
                    analyticsRepository.trackEvent(TextRateEvent())
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    analyticsRepository.trackEvent(TextDontRateEvent())
                }

                DialogInterface.BUTTON_NEUTRAL -> {
                    analyticsRepository.trackEvent(TextRateLaterEvent())
                }
            }
        }
        AppRate.showRateDialogIfMeetsConditions(activity)
    }

    private fun stopSpeak() {
        textToSpeech?.stop()
        text_play?.show()
        text_stop?.hide()
    }

    private fun speak(text: String) {
        mostRecentUtteranceID = (Random().nextInt() % 9999999).toString() + ""
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, mostRecentUtteranceID)

        text_play?.hide()
        text_stop?.show()
    }

    override fun onDetach() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDetach()
    }

    override fun onPause() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onPause()
    }

    override fun onDestroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroy()
    }

    companion object {
        const val ARG_TEXT = "ARG_TEXT"
    }
}