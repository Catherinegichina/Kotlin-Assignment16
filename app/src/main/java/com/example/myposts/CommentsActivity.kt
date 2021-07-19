package com.example.myposts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentsActivity : AppCompatActivity() {
    var postId=0
    lateinit var tvPostTitle:TextView
    lateinit var tvPostody:TextView
//    to access it outside classes.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
//        where we will set the onclick.
        postId=intent.getIntExtra("POST_ID",0)
//    getInXtra-0 is wheb the id is not found.
    castViews()
    getPost()
        }
    fun castViews(){
        tvPostTitle=findViewById(R.id.tvPostTittle)
        tvPostody=findViewById(R.id.tvBody)
    }
    fun getPost(){
        if (postId==0){
        Toast.makeText(baseContext,"Post not found",Toast.LENGTH_LONG).show()
            finish()
    }
        var apiClient=ApiClient.buildAppClient(ApiInterface::class.java)
        var request=apiClient.getPost(postId)
        request.enqueue(object :Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if(response.isSuccessful){
                    var post=response.body()
                    tvPostTitle.text=post?.title
                    tvPostody.text=post?.body
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
              Toast.makeText(baseContext,"failure",Toast.LENGTH_LONG).show()
            }
        })

}
    fun getComments(){
    var rvComments=findViewById<RecyclerView>(R.id.rvComments)
    val retrofit=ApiClient.buildAppClient(ApiInterface::class.java)
    val request=retrofit.getComments(postId)
     request.enqueue(object :Callback<List<Comments>>{
         override fun onResponse(call: Call<List<Comments>>, response: Response<List<Comments>>) {
             if(response.isSuccessful){
                 var comments=response.body()!!
                 var commentsAdapter=commentsAdapter(comments)
                 rvComments.adapter=commentsAdapter
                 rvComments.layoutManager=LinearLayoutManager(baseContext)
         }
     }

         override fun onFailure(call: Call<List<Comments>>, t: Throwable) {
             Toast.makeText(baseContext,t.message,Toast.LENGTH_LONG).show()
         }

    })
    }
}