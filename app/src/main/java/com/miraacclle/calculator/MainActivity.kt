package com.miraacclle.calculator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.miraacclle.calculator.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    lateinit var  binding: ActivityMainBinding
    var number: String? = null

    var  firstNumber: Double = 0.0
    var lastNumber: Double = 0.0

    var status: String? = null
    var operator: Boolean = false

    val myFormatter =  DecimalFormat("######.######")

    var history: String = ""
    var currentResult: String = ""
    var isDot: Boolean = true
    var isEqual: Boolean = false

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        val view =  binding.root
        setContentView(view)
        binding.textViewResult.text  = "0"
        binding.btnZero.setOnClickListener {
            onNumberClicked("0")
        }
        binding.btnOne.setOnClickListener {
            onNumberClicked("1")
        }
        binding.btnTwo.setOnClickListener {
            onNumberClicked("2")
        }
        binding.btnThree.setOnClickListener {
            onNumberClicked("3")
        }
        binding.btnFour.setOnClickListener {
            onNumberClicked("4")
        }
        binding.btnFive.setOnClickListener {
            onNumberClicked("5")
        }
        binding.btnSix.setOnClickListener {
            onNumberClicked("6")
        }
        binding.btnSeven.setOnClickListener {
            onNumberClicked("7")
        }
        binding.btnEight.setOnClickListener {
            onNumberClicked("8")
        }
        binding.btnNine.setOnClickListener {
            onNumberClicked("9")
        }
        binding.btnAc.setOnClickListener{
            onBtnAcClicked()
        }
        binding.btnDel.setOnClickListener{
            number?.let{
                if (it.length == 1) {
                    onBtnAcClicked()
                } else {
                number = it.substring(0,  it.length - 1)
                binding.textViewResult.text = number }
                isDot = !number!!.contains(".")
            }

        }
        binding.btnMulti.setOnClickListener{
            history = binding.textViewHistory.text.toString()
            currentResult = binding.textViewResult.text.toString()
            binding.textViewHistory.text = history.plus(currentResult).plus("*")

            anOperation("multiplication")
        }
        binding.btnDivide.setOnClickListener{
            history = binding.textViewHistory.text.toString()
            currentResult = binding.textViewResult.text.toString()
            binding.textViewHistory.text = history.plus(currentResult).plus("/")

            anOperation("division")
        }
        binding.btnMinus.setOnClickListener{
            history = binding.textViewHistory.text.toString()
            currentResult = binding.textViewResult.text.toString()
            binding.textViewHistory.text = history.plus(currentResult).plus("-")

            anOperation("subtraction")
        }
        binding.btnPlus.setOnClickListener{
            history = binding.textViewHistory.text.toString()
            currentResult = binding.textViewResult.text.toString()
            binding.textViewHistory.text = history.plus(currentResult).plus("+")
            anOperation("addition")
        }
        binding.btnEqual.setOnClickListener{
            history = binding.textViewHistory.text.toString()
            currentResult = binding.textViewResult.text.toString()
            if(operator) {
                when(status) {
                    "multiplication" ->  multiple()
                    "division" ->  divide()
                    "subtraction" ->  minus()
                    "addition" ->  plus()
                    else -> firstNumber = binding.textViewResult.text.toString().toDouble()
                }
                binding.textViewHistory.text = history.plus(currentResult).plus("=").plus(binding.textViewResult.text.toString())

            }

            operator = false
            isDot = !number!!.contains(".")
            isEqual = true
        }
        binding.btnDot.setOnClickListener{
            if (isDot) {
                number = if (number == null) {
                    "0."
                } else if(isEqual){
                    if(binding.textViewResult.text.toString().contains(".")) {
                        binding.textViewResult.text.toString()
                    } else {
                        binding.textViewResult.text.toString().plus(".")
                    }
                } else {
                    "$number."
                }
                binding.textViewResult.text  = number
            }
            isDot = false
        }

        binding.toolBar.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.settings_item -> {
                    val intent = Intent(this, ChangeThemeActivity::class.java)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }
                else ->  return@setOnMenuItemClickListener false
            }
        }
    }
    private fun onBtnAcClicked(){
        number = null
        status = null
        binding.textViewResult.text = "0"
        binding.textViewHistory.text = ""
        firstNumber = 0.0
        lastNumber = 0.0
        isDot = true
        isEqual = false

    }
    private fun anOperation(newStatus: String)  {
        if(operator) {
            when(status) {
                "multiplication" ->  multiple()
                "division" ->  divide()
                "subtraction" ->  minus()
                "addition" ->  plus()
                else -> firstNumber = binding.textViewResult.text.toString().toDouble()
            }
        }

        status = newStatus
        operator = false
        number = null
        isDot = true
    }
    private fun onNumberClicked(clickedNumber: String) {
        if(number == null)  {
            number = clickedNumber
        } else if(isEqual)  {
            number = if(isDot)  {
                clickedNumber
            } else {
                binding.textViewResult.text.toString().plus(clickedNumber)
            }

            firstNumber = number!!.toDouble()
            lastNumber  = 0.0
            status = null
            binding.textViewHistory.text = ""

        }  else {
            number += clickedNumber
        }

        binding.textViewResult.text = number
        operator= true
        isEqual = false
    }
    fun plus() {
        lastNumber = binding.textViewResult.text.toString().toDouble()
        firstNumber += lastNumber
        binding.textViewResult.text = myFormatter.format(firstNumber)
    }
    fun minus() {
        lastNumber = binding.textViewResult.text.toString().toDouble()
        firstNumber -= lastNumber
        binding.textViewResult.text = myFormatter.format(firstNumber)
    }
    private fun multiple() {
        lastNumber = binding.textViewResult.text.toString().toDouble()
        firstNumber *= lastNumber
        binding.textViewResult.text = myFormatter.format(firstNumber)
    }
    private fun divide() {
        lastNumber = binding.textViewResult.text.toString().toDouble()
        if(lastNumber  == 0.0) {
            Toast.makeText(applicationContext,"The divisor can't be zero", Toast.LENGTH_SHORT).show()
        } else {
            firstNumber /= lastNumber
            binding.textViewResult.text = myFormatter.format(firstNumber)
        }
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = this.getSharedPreferences("DarkTheme", Context.MODE_PRIVATE)
        val  isDark  = sharedPreferences.getBoolean("switch", false)
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }  else  {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences = this.getSharedPreferences("calculations", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val resultToSave = binding.textViewResult.text.toString()
        val historyToSave = binding.textViewHistory.text.toString()
        val numberToSave = number
        val statusToSave = status
        val operatorToSave = operator
        val dotToSave = isDot
        val equalToSave = isEqual
        val firstNumberToSave = firstNumber.toString()
        val lastNumberToSave =  lastNumber.toString()

        editor.putString("result",  resultToSave)
        editor.putString("history", historyToSave)
        editor.putString("number", numberToSave)
        editor.putString("status", statusToSave)
        editor.putBoolean("operator", operatorToSave)
        editor.putBoolean("dot", dotToSave)
        editor.putBoolean("equal", equalToSave)
        editor.putString("first", firstNumberToSave)
        editor.putString("last", lastNumberToSave)

        editor.apply()
    }

    override fun onStart() {
        super.onStart()
        sharedPreferences = this.getSharedPreferences("calculations", Context.MODE_PRIVATE)

        binding.textViewResult.text = sharedPreferences.getString("result", "0")
        binding.textViewHistory.text  = sharedPreferences.getString("history", "")

        number = sharedPreferences.getString("number",null)
        status = sharedPreferences.getString("status", null)
        operator = sharedPreferences.getBoolean("operator", false)
        isDot = sharedPreferences.getBoolean("dot", true)
        isEqual = sharedPreferences.getBoolean("equal",  false)
        firstNumber = sharedPreferences.getString("first", "0.0")!!.toDouble()
        lastNumber = sharedPreferences.getString("last", "0.0")!!.toDouble()
    }
}