package com.example.firebasetest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {


    lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Firebase를 선언
        firestore = FirebaseFirestore.getInstance()
//        createData1()
        //createData2()
        //readData()
        //readQueryWhereEqualToData()
        //readQueryWhereGreaterThanData()
        //readQueryWhereGreaterThanOrEqualToData()
        //readQueryWhereLessThanData()
        //readQueryWhereLessThanOrEqualToData()
        //addSnapshotDocument()
        //addSnapshotQuery()
        //updateData()
        //deleteData()
        //getNames()
    }


    data class UserDTO(var name: String? = null, var address: String? = null, var age: Int? = null)

    //create data : input document name directly
    private fun createData1() {
        var userDTO = UserDTO("진지니", "영국", 7)
        firestore?.collection("User")?.document("document1")?.set(userDTO)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    toast("create성공")
            }
    }

    //create data : random document
    //도큐먼트가 이름 랜덤으로 새로 생성되어 값이 들어간다.
    private fun createData2() {
        var userDTO = UserDTO("해서니", "문래동", 25)
        firestore?.collection("User")?.document()?.set(userDTO)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    toast("create성공")
            }
    }


    //read data : pull driven (use you know document name )
    private fun readData() {
        firestore?.collection("User").document("document1").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var userDTO = it.result?.toObject(UserDTO::class.java)
                    toast(userDTO.toString())
                }
            }
    }


    //아래 5개는 검색 쿼리(데이터 요청)를 통한 pull driven 방식

    //1. WhereEqualTo : 쿼리에 입력된 값이 같은 데이터 검색
    //아래를 실행하면 address 가 서울인 document 만 검색된다.
    private fun readQueryWhereEqualToData() {
        firestore?.collection("User").whereEqualTo("address", "대림동").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (dc in it.result!!.documents) {
                        var userDTO = dc.toObject(UserDTO::class.java)
                        toast(userDTO.toString())
                    }
                }
            }
    }

    //2. WhereGenerateThan : 쿼리에 입력된 값을 초과하는 데이터만 검색
    private fun readQueryWhereGreaterThanData() {
        firestore?.collection("User").whereGreaterThan("age", 9).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (dc in it.result!!.documents) {
                        var userDTO = dc.toObject(UserDTO::class.java)
                        toast(userDTO.toString())
                    }
                }
            }
    }

    //3. WhereGreaterThanOrEqualTo : 쿼리에 입력된 값 이상인 데이터
    private fun readQueryWhereGreaterThanOrEqualToData() {
        firestore?.collection("User").whereGreaterThanOrEqualTo("age", 25).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (dc in it.result!!.documents) {
                        var userDTO = dc.toObject(UserDTO::class.java)
                        println(userDTO.toString())
                    }
                }
            }
    }

    //4. WhereLessThan : 쿼리에 입력된 값 미만인 데이터
    private fun readQueryWhereLessThanData() {
        firestore?.collection("User").whereLessThan("age", 9).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (dc in it.result!!.documents) {
                        var userDTO = dc.toObject(UserDTO::class.java)
                        println(userDTO.toString())
                    }
                }
            }
    }

    //5. WhereLessThanOrEqualTo : 쿼리에 입력된 값 이하의 데이터
    private fun readQueryWhereLessThanOrEqualToData() {
        firestore?.collection("User").whereLessThanOrEqualTo("age", 7).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (dc in it.result!!.documents) {
                        var userDTO = dc.toObject(UserDTO::class.java)
                        println(userDTO)
                    }
                }
            }
    }


    //read data : push driven (데이터가 변경되는 순간 읽어옴)
    //실시간으로 반영되는 리스트나 채팅을 만들 때 많이 사용한다.
    //document Id로 검색할 경우, 파라미터로 documentSnapshot 과 firebaseFirestoreException 이 넘어온다.
    private fun addSnapshotDocument() {
        firestore?.collection("User").document("document1")
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                var document = documentSnapshot?.toObject(UserDTO::class.java)
                println(document.toString())
            }
    }

    //쿼리로 검색
    //쿼리로 검색을 할 경우 파라미터로 querySnapshot과 firebaseFirestoreException이 넘어옴
    private fun addSnapshotQuery() {
        firestore?.collection("User").whereEqualTo("name", "해서니")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                for (dc in querySnapshot!!.documentChanges) {
                    var document = dc.document.toObject(UserDTO::class.java)
                    println(document)
                }
            }
    }


    //update(데이터베이스 수정하기)
    //필드값이 존재하지 않으면 필드를 생성, 필드값이 존재하면 업데이트
    private fun updateData() {
        val map = mutableMapOf<String, Any>()
        map["phone"] = "010-5310-3084"
        firestore.collection("User").document("document1").update(map)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    toast("업데이트됨")
                }
            }
    }


    //delete(데이터베이스 삭제)
    private fun deleteData() {
        firestore.collection("User").document("document1")
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    toast("삭제완료")
                }
            }
    }


    //documents 에 있는 name 값을 모두 불러오기
    private fun getNames() {
        val a = firestore.collection("User")
            .get()
            .addOnCompleteListener {
                val querySnapshot = it.result
                querySnapshot?.documents?.map {
                    val name = it.data?.get("name")
                    toast(name.toString())
                }
            }
    }
}
