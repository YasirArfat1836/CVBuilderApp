package com.example.cv_builder

import android.annotation.SuppressLint
import android.util.Log

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.ScrollingView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate


class CVAdapter(private val context: Context, private val cvList: List<CV>) : RecyclerView.Adapter<CVAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text_view)
        val rollNoTextView: TextView = view.findViewById(R.id.roll_no_text_view)
        val cgpaTextView: TextView = view.findViewById(R.id.cgpa_text_view)
        val degreeTextView: TextView = view.findViewById(R.id.degree_text_view)
        val genderTextView: TextView = view.findViewById(R.id.gender_text_view)
        val dobTextView: TextView = view.findViewById(R.id.date_text_view)
        val careerInterestsTextView: TextView = view.findViewById(R.id.interests_text_view)
        val cvNoTextView: TextView = view.findViewById(R.id.cv_no)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder  {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cv = cvList[position]
        // Set the text of the views
        holder.nameTextView.text = cv.getName()
        holder.rollNoTextView.text = cv.getRollNo()
        holder.cgpaTextView.text = cv.getCgpa()
        holder.degreeTextView.text = cv.getDegree()
        holder.genderTextView.text = cv.getGender()
        holder.dobTextView.text = cv.getDob()
        holder.careerInterestsTextView.text = cv.getCareerInterests()
        holder.cvNoTextView.text = position.toString()
    }

    override fun getItemCount(): Int {
        return cvList.size
    }

}


class CV{
    private lateinit var  name: String
    private lateinit var  roll_no: String
    private lateinit var  cgpa: String
    private lateinit var  degree: String
    private lateinit var  gender: String
    private lateinit var  dob: String
    private lateinit var  career_interests: String

    constructor(name: String, rollNo: String, cgpa: String, degree: String, gender: String, dob: String, careerInterests: String) {
        this.name = name
        this.roll_no = rollNo
        this.cgpa = cgpa
        this.degree = degree
        this.gender = gender
        this.dob = dob
        this.career_interests = careerInterests
    }

    // Getter for name
    fun getName(): String {
        return name
    }

    // Setter for name
    fun setName(name: String) {
        this.name = name
    }

    // Getter for roll_no
    fun getRollNo(): String {
        return roll_no
    }

    // Setter for roll_no
    fun setRollNo(rollNo: String) {
        this.roll_no = rollNo
    }

    // Getter for cgpa
    fun getCgpa(): String {
        return cgpa
    }

    // Setter for cgpa
    fun setCgpa(cgpa: String) {
        this.cgpa = cgpa
    }

    // Getter for degree
    fun getDegree(): String {
        return degree
    }

    // Setter for degree
    fun setDegree(degree: String) {
        this.degree = degree
    }

    // Getter for gender
    fun getGender(): String {
        return gender
    }

    // Setter for gender
    fun setGender(gender: String) {
        this.gender = gender
    }

    // Getter for dob
    fun getDob(): String {
        return dob
    }

    // Setter for dob
    fun setDob(dob: String) {
        this.dob = dob
    }

    // Getter for career_interests
    fun getCareerInterests(): String {
        return career_interests
    }

    // Setter for career_interests
    fun setCareerInterests(careerInterests: String) {
        this.career_interests = careerInterests
    }

}

class CVDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "cv_database", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE CVs (id INTEGER PRIMARY KEY AUTOINCREMENT, roll_no TEXT, name TEXT, cgpa TEXT, degree TEXT, gender TEXT, dob TEXT, career_interests TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO: Update the database schema if necessary.
    }

    fun insertCV(cv: CV) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("roll_no", cv.getRollNo())
        values.put("name", cv.getName())
        values.put("cgpa", cv.getCgpa())
        values.put("degree", cv.getDegree())
        values.put("gender", cv.getGender())
        values.put("dob", cv.getDob())
        values.put("career_interests", cv.getCareerInterests())

        db.insert("CVs", null, values)
        Log.d("DB inserted", values.toString())
    }

    @SuppressLint("Range")
    fun retrieveData(): List<CV> {
        val db = readableDatabase
        val cursor = db.query("CVs", null, null, null, null, null, null)

        val cvList = mutableListOf<CV>()
        while (cursor.moveToNext()) {
            val cv = CV(
                name = cursor.getString(cursor.getColumnIndex("name")),
                rollNo = cursor.getString(cursor.getColumnIndex("roll_no")),
                cgpa = cursor.getString(cursor.getColumnIndex("cgpa")),
                degree = cursor.getString(cursor.getColumnIndex("degree")),
                gender = cursor.getString(cursor.getColumnIndex("gender")),
                dob = cursor.getString(cursor.getColumnIndex("dob")),
                careerInterests = cursor.getString(cursor.getColumnIndex("career_interests"))
            )
            cvList.add(cv)
        }
        cursor.close()
        return cvList
    }

}

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    // data for different fields
    private lateinit var selectedName: String
    private lateinit var selectedRollNum: String
    private lateinit var selectedCGPA: String
    private lateinit var selectedInterests:String
    private lateinit var selectedGender:String
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var selectedDate: String
    private lateinit var selectedDegree: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // instantiate cv database helper
        val cvDatabaseHelper = CVDatabaseHelper(this)
        // cv list
        var cvList : List<CV>

        // call handle date picker function for date pick functionality
        handleShowDatePicker()
        // call handle degree spinner to show
        handleDegreeSpinner()

        // generate cv button
        val generateCVBtn = findViewById<Button>(R.id.generateCVBtn)
        // regenerate cv button
        val reGenerateCVBtn = findViewById<Button>(R.id.reGenerateCVBtn)
        // show all CVs button
        val showAllCVsBtn = findViewById<Button>(R.id.showAllCVsBtn)

        val rollnum=findViewById<EditText>(R.id.roll_number_edit_text)
        val name=findViewById<EditText>(R.id.name_edit_text)
        val cgpa =findViewById<EditText>(R.id.cgpa_edit_text)
        val genderRadioGroup = findViewById<RadioGroup>(R.id.gender_radio_group)
        val interest1 = findViewById<CheckBox>(R.id.interest1)
        val interest2 = findViewById<CheckBox>(R.id.interest2)
        val interest3 = findViewById<CheckBox>(R.id.interest3)


        // get cv name text views to set their values
        val name_cv = findViewById<TextView>(R.id.name_text_view)
        val rollnum_cv = findViewById<TextView>(R.id.roll_no_text_view)
        val cgpa_cv = findViewById<TextView>(R.id.cgpa_text_view)
        val date_cv = findViewById<TextView>(R.id.date_text_view)
        val gender_cv = findViewById<TextView>(R.id.gender_text_view)
        val degree_cv = findViewById<TextView>(R.id.degree_text_view)
        val interests_cv = findViewById<TextView>(R.id.interests_text_view)


        selectedDate = LocalDate.now().toString()


        val cvTemplateLayout = findViewById<LinearLayout>(R.id.cvTemplate)
        val cvGeneratorTemplateLayout = findViewById<LinearLayout>(R.id.cvGeneratorTemplate)

        // default values
        cvTemplateLayout.visibility = View.GONE
        cvGeneratorTemplateLayout.visibility = View.VISIBLE


        selectedGender = "Male"
        genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.male_radio_button -> selectedGender = "Male"
                R.id.female_radio_button -> selectedGender = "Female"
                R.id.other_radio_button -> selectedGender = "Other"
            }
        }


        generateCVBtn.setOnClickListener {

            // toggling visibility of the cv and cv builder
            if (cvTemplateLayout.visibility == View.VISIBLE) {
                cvTemplateLayout.visibility = View.GONE
                cvGeneratorTemplateLayout.visibility = View.VISIBLE
            }
            else {
                cvTemplateLayout.visibility = View.VISIBLE
                cvGeneratorTemplateLayout.visibility = View.GONE
            }

            selectedInterests = ""

            if ( interest1.isChecked)
                selectedInterests += interest1.text.toString() + " "
            if ( interest2.isChecked)
                selectedInterests += interest2.text.toString() + " "
            if ( interest3.isChecked)
                selectedInterests += interest3.text.toString() + " "

            selectedName = name.text.toString()
            selectedRollNum = rollnum.text.toString()
            selectedCGPA = cgpa.text.toString()



            // set values to cv
            name_cv.text = selectedName
            rollnum_cv.text = selectedRollNum
            cgpa_cv.text = selectedCGPA
            degree_cv.text = selectedDegree
            gender_cv.text = selectedGender
            interests_cv.text = selectedInterests
            date_cv.text = selectedDate

            // create an object for CV
            val cv = CV(selectedName, selectedRollNum, selectedCGPA, selectedDegree, selectedGender, selectedDate, selectedInterests)
            // insert into SQL Table
            cvDatabaseHelper.insertCV(cv)
        }

        reGenerateCVBtn.setOnClickListener {
            // toggling visibility of the cv and cv builder
            if (cvTemplateLayout.visibility == View.VISIBLE) {
                cvTemplateLayout.visibility = View.GONE
                cvGeneratorTemplateLayout.visibility = View.VISIBLE
            } else {
                cvTemplateLayout.visibility = View.VISIBLE
                cvGeneratorTemplateLayout.visibility = View.GONE
            }

            // reset the previous data
            name.setText("")
            rollnum.setText("")
            cgpa.setText("")
            interest1.isChecked = false
            interest2.isChecked = false
            interest3.isChecked = false
        }

        showAllCVsBtn.setOnClickListener {
            val recyclerView = findViewById<RecyclerView>(R.id.cv_recyclerview)
            cvList = cvDatabaseHelper.retrieveData()
            val cvListAdapter = CVAdapter(this, cvList)
            recyclerView.adapter = cvListAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            cvListAdapter.notifyDataSetChanged()
            showAllCVsBtn.visibility = View.GONE
        }

    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val toggleDatePickerButton = findViewById<Button>(R.id.toggle_date_picker_button)
        selectedDate = "$year-$month-$dayOfMonth"
        toggleDatePickerButton.setText(selectedDate)
    }

    @SuppressLint("NewApi")
    fun handleShowDatePicker()
    {
        val toggleDatePickerButton = findViewById<Button>(R.id.toggle_date_picker_button)
        datePickerDialog = DatePickerDialog(this)

        // Set the click listener for the button
        toggleDatePickerButton.setOnClickListener {
            // Toggle the date picker
            if (datePickerDialog.isShowing) {
                datePickerDialog.dismiss()
            } else {
                datePickerDialog.show()
            }
        }

        datePickerDialog.setOnDateSetListener(this)

    }

    fun handleDegreeSpinner():String
    {
        val degreesList = listOf<String>("BSIT", "BSCS", "BSSE")
        val degreesSpinner = findViewById<Spinner>(R.id.degrees_spinner)
        val degreesListAdaptor = ArrayAdapter(this, android.R.layout.simple_spinner_item, degreesList)
        degreesListAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        degreesSpinner.adapter = degreesListAdaptor
        selectedDegree = degreesSpinner.selectedItem as String

        degreesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedDegree = parent.getItemAtPosition(position) as String
                selectedDegree = degreesSpinner.selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("No need to implement")
            }
        }
        return selectedDegree
    }
}