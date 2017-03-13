package cn.stonepyy.smswriter

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.content.Intent
import android.text.TextUtils
import android.content.ContentValues
import android.net.Uri
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_smswriter.*


class SMSWriterActivity : AppCompatActivity() {

    private val RESULT_SMS_APP = 1
    private var defaultSMSApp = ""
    private var mToast : Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smswriter)
        initData()
        initUI()
    }

    private fun initData() {
        defaultSMSApp = getDefaultSMSAPP(this)
    }

    private fun initUI() {
        smsWrite.setOnClickListener {
            if (!isDefaultSMSAPP()) {
                showToast("请设置当前应用为默认短信应用")
                return@setOnClickListener
            }
            writeSMS()
        }
        doDefaultApp.setOnClickListener {
            requestDefaultSMSPermission()
        }
        revertDefaultApp.setOnClickListener {
            revertDefaultSMSPermission()
        }
    }

    private fun requestDefaultSMSPermission() {

        if (isDefaultSMSAPP()) {
            return
        }
        val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,packageName)
        startActivityForResult(intent,RESULT_SMS_APP)
    }

    private fun revertDefaultSMSPermission() {

        if (!isDefaultSMSAPP() && TextUtils.isEmpty(defaultSMSApp)) {
            return
        }
        val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,defaultSMSApp)
        startActivityForResult(intent,RESULT_SMS_APP)

    }

    private fun writeSMS() {
        val resolver = contentResolver
        val uri = Uri.parse("content://sms/inbox")
        val values = ContentValues()
        values.put("address", smsSender.text.toString())
        values.put("type", Telephony.Sms.MESSAGE_TYPE_INBOX)
        values.put("body", smsContent.text.toString())
        values.put("date", calendarView.date)
        resolver.insert(uri, values)
        showToast("短信写入成功")
    }

    private fun isDefaultSMSAPP():Boolean {
        return getDefaultSMSAPP(this) == packageName
    }

    private fun showToast(msg : String) {
        if (mToast == null) {
            mToast = Toast.makeText(this,msg,Toast.LENGTH_SHORT)
        } else {
            mToast!!.setText(msg)
        }
        mToast!!.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == RESULT_SMS_APP) {
            showToast("应用变换成功")
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    companion object {
        private fun getDefaultSMSAPP(context: Context) : String {
            return Telephony.Sms.getDefaultSmsPackage(context)
        }
    }
}
