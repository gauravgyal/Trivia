package com.gauravgoyal.trivia.widget

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.gauravgoyal.trivia.R
import com.gauravgoyal.trivia.databinding.ViewQuestionBinding
import com.gauravgoyal.trivia.model.Question

class QuestionView : ConstraintLayout, OptionView.OptionClickListener {


    private lateinit var binding: ViewQuestionBinding

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val props = context.obtainStyledAttributes(attrs, R.styleable.QuestionView, 0, 0)
        binding =
                DataBindingUtil.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater, R.layout.view_question, this, true)
        props.recycle()
        addClickListners()

    }


    override fun onOptionSelection(view: OptionView, choice: String) {
        listener?.onAnswer(view, choice)
    }

    private fun addClickListners() {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is OptionView) view.optionClickListener = this
        }
    }

    fun setQuestion(question: Question) {
        binding.question = question
    }

    internal fun disableOptions(clicked: OptionView) {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is OptionView && view.id != clicked.id) view.disable()
        }
    }


    internal var listener: QuestionViewListener? = null

    interface QuestionViewListener {
        fun onAnswer(view: OptionView, choice: String)
    }
}

