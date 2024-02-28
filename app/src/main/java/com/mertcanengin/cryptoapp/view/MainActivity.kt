package com.mertcanengin.cryptoapp.view






import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mertcanengin.cryptoapp.R
import com.mertcanengin.cryptoapp.adapter.RecyclerViewAdapter
import com.mertcanengin.cryptoapp.databinding.ActivityMainBinding
import com.mertcanengin.cryptoapp.model.CryptoModel
import com.mertcanengin.cryptoapp.service.CryptoAPI
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),RecyclerViewAdapter.Listener {
    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var  recyclerViewAdapter : RecyclerViewAdapter? = null
    //Disposable RXJava
    private var compositeDisposable: CompositeDisposable?=null



    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        compositeDisposable= CompositeDisposable()



        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager // recyclerView tanımını binding üzerinden yapın

        loadData()
    }


    private  fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()



        val service = retrofit.create(CryptoAPI::class.java)
        val call = service.getData()

        call.enqueue(object: Callback<List<CryptoModel>> {
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if(response.isSuccessful) {

                    response.body()?.let {
                        cryptoModels = ArrayList(it)

                        cryptoModels?.let {
                            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

                            recyclerViewAdapter= RecyclerViewAdapter(it,this@MainActivity)
                            recyclerView.adapter = recyclerViewAdapter

                        }
                        /*
                        for (cryptoModel: CryptoModel in cryptoModels!!) {
                            println(cryptoModel.currency)
                            println(cryptoModel.price)
                        }
*/

                    }
                }


            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })





    }
    /*
    private fun handleResponse(cryptoList:List<CryptoModel>) {
        cryptoModels?.let {
            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
            recyclerView.adapter = recyclerViewAdapter

        }


    }
     */


    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this, "clicked: ${cryptoModel.currency}", Toast.LENGTH_LONG).show()
    }
/*
    override fun onDestroy() {
            super.onDestroy()
        compositeDisposable?.clear()
    }
    */

}