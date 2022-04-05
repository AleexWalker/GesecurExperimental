package com.gesecur.app.data.gesecur

import com.gesecur.app.data.gesecur.responses.BaseResponse
import com.gesecur.app.data.gesecur.responses.BaseResponseNullable
import com.gesecur.app.data.gesecur.responses.LoginResponse
import com.gesecur.app.data.gesecur.responses.OperationsResponse
import com.gesecur.app.domain.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface GesecurService {

    /**
     * Devices
     */
    @POST("devices/gcm/")
    suspend fun createDevice( )

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): BaseResponse<LoginResponse>

    @POST("vigilante/login")
    @FormUrlEncoded
    suspend fun loginVigilante(
        @Field("codigo") code: String
    ): BaseResponse<LoginResponse>

    @GET("usuario/actual/{user}")
    suspend fun getCurrentUser(
        @Path("user") userId: Long
    ): BaseResponse<User>

    @GET("usuario/actual/vigilante/{user}")
    suspend fun getCurrentVigilante(
        @Path("user") userId: Long
    ): BaseResponse<User>

    @GET("app/partes_trabajo/{user}/{dateini}")
        suspend fun getWorkParts(
            @Path("user") userId: Long,
            @Path("dateini") dateIni: String
    ): BaseResponse<List<WorkPart>>

    @GET("partes_trabajo/{user}/{part_id}")
    suspend fun getWorkPartDetail(
            @Path("user") userId: Long,
            @Path("part_id") partId: Long
    ): BaseResponse<WorkPart>

    @GET("ordenes-trabajo/servicios/{user}/{ot_id}")
    suspend fun getWorkOrderServices(
        @Path("user") userId: Long,
        @Path("ot_id") otId: Long
    ): BaseResponse<List<Service>>

    @GET("trabajos_cod/{user}")
    suspend fun getCodifiedJobs(
            @Path("user") userId: Long
    ): BaseResponse<List<CodifiedJob>>

    @POST("partes_trabajo/trabajo/add")
    @FormUrlEncoded
    suspend fun addJob(
        @Field("personal_id") personalId: Long,
        @Field("trabajo_id") jobId: Long = -1,
        @Field("parte_id") partId: Long = -1,
        @Field("trabajo_descripcion") desc: String?,
        @Field("duracion") duration: Int,
    ): BaseResponse<Long>

    @POST("partes_trabajo/trabajo/update")
    @FormUrlEncoded
    suspend fun updateJob(
        @Field("personal_id") personalId: Long,
        @Field("parte_id") partId: Long,
        @Field("trabajo_id") jobId: Long,
        @Field("parte_trabajo_id") workPartId: Long,
        @Field("duracion") duration: Int,
        @Field("cantidad") quantity: Int,
        @Field("extra") extra: Int,
    ): OperationsResponse

    @POST("partes_trabajo/trabajo/update")
    @FormUrlEncoded
    suspend fun updateJobChecked(
        @Field("personal_id") personalId: Long,
        @Field("parte_id") partId: Long,
        @Field("checked") extra: Int
    ): OperationsResponse

    @POST("partes_trabajo/trabajo/delete")
    @FormUrlEncoded
    suspend fun deleteJob(
            @Field("usuario") userId: Long,
            @Field("parte_id") partId: Long,
            @Field("trabajo_id") jobId: Long,
            @Field("parte_trabajo_id") workPartId: Long,

    ): OperationsResponse

    @POST("partes_trabajo/close")
    @FormUrlEncoded
    suspend fun closePart(
        @Field("parte_id") partId: Long,
        @Field("personal_id") personalId: Long,
        @Field("dni") dni: String,
        @Field("observaciones") observaciones: String,
        @Field("finaliza_correctamente") faults: Int
    ) : OperationsResponse

    @GET("productos/{user}")
    suspend fun getAvailableProducts(
            @Path("user") userId: Long,
    ): BaseResponse<List<Product>>

    @POST("partes_trabajo/material/add")
    @FormUrlEncoded
    suspend fun addMaterial(
        @Field("personal_id") personalId: Long,
        @Field("producto_id") productId: Long,
        @Field("parte_id") partId: Long = -1L
    ): BaseResponse<Long>

    @POST("partes_trabajo/material/update")
    @FormUrlEncoded
    suspend fun updateMaterial(
        @Field("parte_id") partId: Long,
        @Field("parte_material_id") partMaterialId: Long,
        @Field("cantidad") quantity: Int,
        @Field("extra") extra: Int
    ): OperationsResponse

    @POST("partes_trabajo/otros/update")
    @FormUrlEncoded
    suspend fun updateOtherQuantity(
       @Field("parte_id") partId: Long,
       @Field("parte_otros_id") partMaterialId: Long,
       @Field("cantidad") quantity: Int
    ): OperationsResponse



    @POST("partes_trabajo/personal/start")
    @FormUrlEncoded
    suspend fun startWork(
        @Field("personal_id") personalId: Long,
        @Field("parte_id") partId: Long,
        @Field("parte_personal_id") partPersonalId: Long,
        @Field("lat") lat: Double,
        @Field("lon") lon: Double
    ): OperationsResponse

    @POST("partes_trabajo/personal/finish")
    @FormUrlEncoded
    suspend fun finishWork(
        @Field("personal_id") personalId: Long,
        @Field("parte_id") partId: Long,
        @Field("parte_personal_id") partPersonalId: Long,
        @Field("lat") lat: Double,
        @Field("lon") lon: Double,
        @Field("horas_especiales") specialTime: Int
    ): OperationsResponse


    /** Incidences **/
    @GET("app/tecnico/incidencias/{personal_id}")
    suspend fun getUserIncidences(
        @Path("personal_id") personalId: Long,
    ): BaseResponse<List<Incidence>>

    @GET("app/tecnico/incidencias/tipos/{user_id}")
    suspend fun getTechnicalIncidenceTypes(
        @Path("user_id") userId: Long,
    ): BaseResponse<List<IncidenceType>>

    @POST("app/incidencia/tecnico/add")
    @Multipart
    suspend fun addIncidence(
        @Part("incidencia_cod_id") typeId: RequestBody,
        @Part("descripcion") description: RequestBody,
        @Part("localizacion") location: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("personal_id") personalId: RequestBody,
        @Part file: MultipartBody.Part
    ) : OperationsResponse


    /** Vigilant **/

    @GET("app/incidencias/vigilante/{vigilante_id}")
    suspend fun getVigilantIncidences(
        @Path("vigilante_id") personalId: Long,
    ): BaseResponse<List<VigilantIncidence>>

    @GET("app/incidencia/tipos/{vigilante_id}")
    suspend fun getVigilantIncidenceTypes(
        @Path("vigilante_id") userId: Long,
    ): BaseResponse<List<IncidenceVigilantType>>

    @POST("app/incidencia/add")
    @Multipart
    suspend fun addVigilantIncidence(
        @Part("incidencia_tipo_id") typeId: RequestBody,
        @Part("descripcion") description: RequestBody,
        @Part("localizacion") location: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("vigilante_id") vigilanteId: RequestBody,
        @Part file: MultipartBody.Part
    ) : OperationsResponse

    @GET("app/turnos/{vigilante_id}")
    suspend fun getVigilantTurns(
        @Path("vigilante_id") userId: Long,
    ): BaseResponse<List<Turn>>

   @GET("app/turnos/registros/{vigilante_id}/{turno_id}")
   suspend fun getTurnIdRegistries(
       @Path("vigilante_id") userId: Long,
       @Path("turno_id") turnId: Long
   ): BaseResponse<List<NewsRegistry>>

   @POST("app/turno/registros/add")
   @FormUrlEncoded
   suspend fun addNewTurnRegistry(
       @Field("vigilante_id") personalId: Long,
       @Field("turno_id") turnId: Long,
       @Field("registro_tipo_id") registryTypeId: Int,
   ): OperationsResponse

   @POST("app/turno/observaciones/add")
   @FormUrlEncoded
   suspend fun addNewTurnObservation(
       @Field("vigilante_id") personalId: Long,
       @Field("turno_id") turnId: Long,
       @Field("descripcion") desc: String,
   ): OperationsResponse

    @POST("app/turno/start")
    @FormUrlEncoded
    suspend fun startTurn(
        @Field("vigilante_id") userId: Long,
        @Field("lat") lat: Double,
        @Field("lon") lon: Double,
    ): BaseResponse<Long>

    @POST("app/turno/finish")
    @FormUrlEncoded
    suspend fun finishTurn(
        @Field("vigilante_id") userId: Long,
        @Field("turno_id") turnId: Long,
        @Field("lat") lat: Double,
        @Field("lon") lon: Double,
    ): OperationsResponse


    /*** Personal ***/

    @GET("empleado/gastos/{user_id}/{personal_id}")
    suspend fun getUserExpenses(
        @Path("user_id") userId: Long,
        @Path("personal_id") personalId: Long
    ): BaseResponseNullable<List<Expense>>

    @GET("empleado/tipos/gastos/{user_id}")
    suspend fun getUserExpenseTypes(
        @Path("user_id") userId: Long
    ): BaseResponse<List<ExpenseType>>

    @POST("empleado/gastos/add")
    @Multipart
    suspend fun addUserExpense(
        @Part("usuario") user: RequestBody,
        @Part("parte_id") partId: RequestBody?,
        @Part("personal_id") personalId: RequestBody,
        @Part("fecha") date: RequestBody,
        @Part("cantidad") quantity: RequestBody,
        @Part("tipo_gasto_id") type: RequestBody,
        @Part("nota") desc: RequestBody,
        @Part("importe") price: RequestBody,
        @Part file: MultipartBody.Part
    ) : OperationsResponse

    @POST("empleado/gastos/delete")
    @FormUrlEncoded
    suspend fun deleteExpense(
        @Field("usuario") userId: Long,
        @Field("personal_gasto_id") expenseId: Long
    ) : OperationsResponse

    //Mileage

    @GET("empleado/kilometraje/{user_id}/{personal_id}")
    suspend fun getUserMileage(
        @Path("user_id") userId: Long,
        @Path("personal_id") personalId: Long
    ): BaseResponseNullable<List<Mileage>>

    @GET("vehiculos/{user_id}")
    suspend fun getUserVehicles(
        @Path("user_id") userId: Long
    ): BaseResponse<List<Vehicle>>

    @POST("empleado/kilometraje/add")
    @Multipart
    suspend fun addUserMileage(
        @Part("usuario") user: RequestBody,
        @Part("parte_id") partId: RequestBody?,
        @Part("personal_id") personalId: RequestBody,
        @Part("fecha") date: RequestBody,
        @Part("km_inicial") kmIni: RequestBody,
        @Part("km_final") kmEnd: RequestBody,
        @Part("vehiculo_id") vehicleId: RequestBody,
        @Part("nota") desc: RequestBody,
        @Part file: MultipartBody.Part
    ) : OperationsResponse

    @POST("empleado/kilometraje/delete")
    @FormUrlEncoded
    suspend fun deleteMileage(
        @Field("usuario") userId: Long,
        @Field("personal_kilometraje_id") mileageId: Long
    ) : OperationsResponse

    /***
     * Planificaciones
     */

    @POST("planificaciones/start")
    @FormUrlEncoded
    suspend fun startPlanification(
        @Field("planificacion_id") planiId: Long,
        @Field("usuario") usuario: Long,
        @Field("personal_id") personalId: Long,
    ): BaseResponseNullable<Long>

    @GET("app/planificaciones/{personal_id}/{date}")
    suspend fun getPlanificationsByDate(
        @Path("personal_id") personalId: Long,
        @Path("date") date: String
    ): BaseResponse<List<WorkPlanification>>

    @GET("app/planificaciones/{personal_id}/{plani_id}/{date}")
    suspend fun getPlanificationDetailByDate(
        @Path("personal_id") personalId: Long,
        @Path("plani_id") planiId: Long,
        @Path("date") date: String
    ): BaseResponse<WorkPlanification>

    //Attachments
    @POST("parte/adjuntos/add")
    @Multipart
    suspend fun addAttachment(
        @Part("usuario") user: RequestBody,
        @Part("parte_id") partId: RequestBody?,
        @Part("personal_id") personalId: RequestBody,
        @Part("fecha") date: RequestBody,
        @Part("nota") desc: RequestBody,
        @Part file: MultipartBody.Part
    ): OperationsResponse
}