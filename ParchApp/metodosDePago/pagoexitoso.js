let pagina = document.querySelector("#pagina");


window.addEventListener('DOMContentLoaded', function() {
    const datosPago = JSON.parse(localStorage.getItem('datosPago'));
    
    if (datosPago) {
        // Mostrar los datos en la página
        console.log('Datos del pago:', datosPago);
        
        // Ejemplo: mostrar en el HTML
        const contenedorDatos = document.getElementById('datos-pago');
        if (contenedorDatos) {
            contenedorDatos.innerHTML = `
            <h2 style= "margin-top: 100px;"> RESUMEN DE PAGO</h2>
                
                <tr> 
                    <th><strong>Método:</strong></th>
                    <td>${datosPago.metodoPago}</td>
                    <th><strong>Tarjeta:</strong></th>
                    <td>${datosPago.tipoTarjeta}</td>
                    <th><strong>Valor:</strong></th>
                    <td> </td>
                </tr>
                <tr>
                    <th><strong>Últimos 4 dígitos:</strong></th> 
                    <td>${datosPago.numeroTarjeta.slice(-4)}</td>
                    <th><strong>Titular:</strong></th>
                    <td>${datosPago.nombreTitular}</td>
                    <th><strong>Plan:</strong></th>
                    <td> </td>
                </tr>
            `;
        }

        console.log(typeof contenedorDatos);
        pagina.appendChild(contenedorDatos);
        
        // Limpiar el localStorage después de usar los datos
        // localStorage.removeItem('datosPago');
    }
});