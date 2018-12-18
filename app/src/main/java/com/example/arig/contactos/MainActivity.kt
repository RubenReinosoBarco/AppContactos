package com.example.arig.contactos

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.app.SearchManager
import android.support.v7.widget.SearchView
import android.widget.GridView
import android.widget.Switch
import android.widget.ViewSwitcher


class MainActivity : AppCompatActivity() {

    var lista:ListView? = null
    var grid:GridView? = null
    var viewSwitcher : ViewSwitcher? = null

    companion object {
        var contactos: ArrayList<Contacto>? = null
        var adaptador: AdaptadorCustom? = null
        var adaptadorGrid: AdaptadorCustomGrid? = null

        fun agregarContacto(contacto: Contacto){
            adaptador?.addItem(contacto)

        }

        fun obtenerContacto(index:Int):Contacto{
            return adaptador?.getItem(index) as Contacto

        }

        fun eliminarContacto(index:Int){
            adaptador?.removeItem(index)
        }
        fun actualizarContacto(index: Int, nuevoContacto: Contacto){
            adaptador?.updateItem(index, nuevoContacto)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        contactos = ArrayList()
        contactos?.add(Contacto("Marcos","Rivas", "Ebay", 35, 80.5f, "C/Mayor 1", "7654435670", "marcosR@ebay.es", R.drawable.foto_01))
        contactos?.add(Contacto("luis","Rivas", "Amazon", 35, 80.5f, "C/Mayor 1", "7654435670", "marcosR@ebay.es", R.drawable.foto_02))
        contactos?.add(Contacto("Maria","Rivas", "Zara", 35, 80.5f, "C/Mayor 1", "7654435670", "marcosR@ebay.es", R.drawable.foto_03))
        contactos?.add(Contacto("Luisa","Rivas", "Telepizza", 35, 80.5f, "C/Mayor 1", "7654435670", "marcosR@ebay.es", R.drawable.foto_04))

        lista = findViewById(R.id.lista)
        grid = findViewById(R.id.grid)

        adaptador = AdaptadorCustom(this, contactos!!)
        adaptadorGrid = AdaptadorCustomGrid(this, contactos!!)

        lista?.adapter = adaptador
        grid?.adapter = adaptadorGrid

        viewSwitcher = findViewById(R.id.viewSwitcher)


        lista?.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, Detalle::class.java)
            intent.putExtra("ID", position.toString())
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val itemBusqueda = menu?.findItem(R.id.searchView)
        val searchView = itemBusqueda?.actionView as SearchView

        val itemSwitch = menu?.findItem(R.id.switchView)
        itemSwitch?.setActionView(R.layout.switch_item)
        val switchView = itemSwitch?.actionView?.findViewById<Switch>(R.id.sCambiaVista)


        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Buscar Contacto..."

        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            // Preparar los datos
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                // filtrar
                adaptador!!.filtrar(newText!!)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                // filtrar
                return true
            }
        })

        switchView?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewSwitcher?.showNext()
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.Nuevo->{
                val intent = Intent(this, Nuevo::class.java)
                startActivity(intent)
                return true
            }
            else ->{return super.onOptionsItemSelected(item)}
        }

       // return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        adaptador?.notifyDataSetChanged()

    }
}
