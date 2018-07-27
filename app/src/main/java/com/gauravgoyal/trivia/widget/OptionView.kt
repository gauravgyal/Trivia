package com.gauravgoyal.trivia.widget

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import com.gauravgoyal.trivia.R
import com.gauravgoyal.trivia.databinding.ViewAnsweredOptionProgressBinding
import com.gauravgoyal.trivia.utlis.dualReveal

class OptionView : ConstraintLayout {
    private lateinit var binding: ViewAnsweredOptionProgressBinding
    private var unselectionColor: Int = 0
    private var selectionColor: Int = 0

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val props = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton, 0, 0)

        //introduces for text color , right now not using it
        selectionColor =
                props.getColor(R.styleable.ProgressButton_selection_color, ContextCompat.getColor(context, R.color.white))
        unselectionColor =
                props.getColor(R.styleable.ProgressButton_unselection_color, ContextCompat.getColor(context, R.color.trivia_text_color))
        val buttonText = props.getString(R.styleable.ProgressButton_choice)
        binding =
                DataBindingUtil.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater, R.layout.view_answered_option_progress, this, true)
        binding.root.setOnClickListener { view ->
            if (!this.isSelected) this.isSelected = !this.isSelected
            optionClickListener?.onOptionSelection(this, binding.optionText.text.toString())
            dualReveal(binding.optionText, binding.bg)
        }
        setOption(buttonText.orEmpty())
        props.recycle()

    }


    fun setOption(text: String) {
        binding.optionText.text = text
        binding.bg.text = text
    }

    fun disable() {
        binding.root.isClickable = false
    }


    var optionClickListener: OptionClickListener? = null


    interface OptionClickListener {
        fun onOptionSelection(view: OptionView, choice: String)
    }

}

