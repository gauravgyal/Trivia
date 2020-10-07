package com.gauravgoyal.trivia.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import com.gauravgoyal.trivia.R
import com.gauravgoyal.trivia.databinding.ActivityMainBinding
import com.gauravgoyal.trivia.utlis.logd
import com.gauravgoyal.trivia.utlis.scaleUpFromCenter
import com.gauravgoyal.trivia.viewModel.TriviaViewModel
import com.gauravgoyal.trivia.widget.OptionView
import com.gauravgoyal.trivia.widget.QuestionView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_question.*


class MainActivity : AppCompatActivity(), QuestionView.QuestionViewListener {

        private val mHandler by lazy { Handler() }
    private val activityVM by lazy {
        ViewModelProviders.of(this).get(TriviaViewModel::class.java)
    }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        observeQuestion()
        activityVM.getData()
    }

    private fun initUI() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.layout.listener = this
    }

    override fun onAnswer(view: OptionView, choice: String) {
        logd(choice)
        binding.layout.disableOptions(view)
    }

    private fun observeQuestion() {
        activityVM.question.observe(this, Observer { question ->
            binding.question = question
            showQuestion()
            animateQuestion()
        })
    }

    @VisibleForTesting
    private fun showQuestion() {
        mHandler.postDelayed(Runnable { ques.scaleUpFromCenter() }, 1500)
    }

    @VisibleForTesting
    private fun animateQuestion() {
        mHandler.postDelayed(Runnable { questionStmtAnim() }, 4000)
    }

    @VisibleForTesting
    private fun showOptions() {
        mHandler.postDelayed(Runnable { group.show() }, 200)
    }

    private fun questionStmtAnim() {
        TransitionManager.beginDelayedTransition(constraintLayout)
        placeholder.apply {
            setContentId(ques.id)
            postOnAnimation {
                val anim = ques.animate()
                anim.scaleX(.8f).scaleY(0.8f).duration = 300
                showOptions()
            }
        }
    }

}
