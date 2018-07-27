package com.gauravgoyal.trivia.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.gauravgoyal.trivia.model.Question
import com.gauravgoyal.trivia.utlis.getQuestion

class TriviaViewModel : ViewModel() {


    var question: MutableLiveData<Question> = MutableLiveData()

    fun getData() {
        question.value = getQuestion()
    }

}
