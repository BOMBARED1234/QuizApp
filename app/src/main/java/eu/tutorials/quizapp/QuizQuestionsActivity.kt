package eu.tutorials.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import eu.tutorials.quizapp.databinding.ActivityQuizQuestionsBinding

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityQuizQuestionsBinding

    private var mCurrentPosition: Int = 1
    private var mQuestionList:ArrayList<Question>? = null
    private var mSelectedOptionPosition:Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUsername:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //BINDING THE IDS IN LAYOUT DESIGNS
        binding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // get intent from main activity - username
        mUsername = intent.getStringExtra(Constants.USER_NAME)

        // GET QUESTION OBJECT IN CONSTANT FILE
        mQuestionList = Constants.getQuestions()
        setQuestion()
        defaultOptionsView()

        // set the onclick listener
        binding.tvOptionOne?.setOnClickListener(this)
        binding.tvOptionTwo?.setOnClickListener(this)
        binding.tvOptionThree?.setOnClickListener(this)
        binding.tvOptionFour?.setOnClickListener(this)
        binding.btnSubmit?.setOnClickListener(this)
    }

    private fun setQuestion() {

        val question: Question? = mQuestionList!![mCurrentPosition - 1]
        binding.ivImage?.setImageResource(question!!.image)
        binding.progressBar.progress = mCurrentPosition
        binding.tvProgress.text = "$mCurrentPosition" + "/" + binding.progressBar.max
        binding.tvQuestion.text = question!!.question
        binding.tvOptionOne.text = question.optionOne
        binding.tvOptionTwo.text = question.optionTwo
        binding.tvOptionThree.text = question.optionThree
        binding.tvOptionFour.text = question.optionFour

        if(mCurrentPosition == mQuestionList!!.size){
            binding.btnSubmit?.text = "FINISH"
        }else{
            binding.btnSubmit?.text = "SUBMIT"
        }
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        binding.tvOptionOne?.let {
            options.add(0,it)
        }
        binding.tvOptionTwo?.let {
            options.add(1,it)
        }
        binding.tvOptionThree?.let {
            options.add(2,it)
        }
        binding.tvOptionFour?.let {
            options.add(3,it)
        }

        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }

    private fun selectedOptionView(tv:TextView, selectedOptionNum:Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363a43"))
        tv.setTypeface(tv.typeface,Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    override fun onClick(view: View?) {
        defaultOptionsView()
        when(view?.id){
            R.id.tvOptionOne -> {
                binding.tvOptionOne?.let {
                    selectedOptionView(it,1)
                }
            }

            R.id.tvOptionTwo -> {
                binding.tvOptionTwo?.let {
                    selectedOptionView(it,2)
                }
            }

            R.id.tvOptionThree -> {
                binding.tvOptionThree?.let {
                    selectedOptionView(it,3)
                }
            }

            R.id.tvOptionFour -> {
                binding.tvOptionFour?.let {
                    selectedOptionView(it,4)
                }
            }
            R.id.btnSubmit -> {
                if(mSelectedOptionPosition == 0){
                    mCurrentPosition++
                    when{
                        mCurrentPosition <= mQuestionList!!.size ->{
                            setQuestion()
                        }
                        else->{
                            val intent = Intent(this,ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME,mUsername)
                            intent.putExtra(Constants.CORRECT_ANSWERS,mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS,mQuestionList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }else{
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    if(question!!.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition,R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionList!!.size) {
                        binding.btnSubmit.text = "FINISH"
                    } else {
                        binding.btnSubmit.text = "GO TO NEXT QUESTION"

                    }

                    mSelectedOptionPosition = 0


                }
            }
        }
    }
    private fun answerView(answer:Int,drawableView:Int){
        when(answer){
            1 -> {
                binding.tvOptionOne?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            2 -> {
                binding.tvOptionTwo?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            3 -> {
                binding.tvOptionThree?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            4 -> {
                binding.tvOptionFour?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
        }
    }
}