package br.com.silvio.microredesocial.utils

import android.Manifest
import android.content.Context
import android.location.Geocoder
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

class LocalizacaoHelper(
    private val context: Context
) {

    interface Callback{
        fun onCidadeRecebida(cidade:String)
        fun onErro(msg:String)
    }

    @RequiresPermission(anyOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ])
    fun obterCidade(callback: Callback){

        val client=
            LocationServices.getFusedLocationProviderClient(context)

        val request=
            CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build()

        client.getCurrentLocation(request,null)
            .addOnSuccessListener { location ->

                if(location==null){
                    callback.onErro("Localização indisponível")
                    return@addOnSuccessListener
                }

                try{
                    val geocoder=
                        Geocoder(context, Locale.getDefault())

                    val lista=
                        geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )

                    if(!lista.isNullOrEmpty()){
                        val cidade=
                            lista[0].locality
                                ?: lista[0].subAdminArea
                                ?: "Cidade desconhecida"

                        callback.onCidadeRecebida(cidade)
                    }

                }catch(e:Exception){
                    callback.onErro(e.message ?: "Erro GPS")
                }
            }
    }
}