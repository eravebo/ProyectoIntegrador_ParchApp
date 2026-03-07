let espacioTarjetas = document.querySelector("#tarjetas");
let espacioBancos = document.querySelector("#bancos")
let espacioTranferencias = document.querySelector("#Transferencias")
let formaPag = document.querySelector("#seleccionForma");
let seleccionTarjeta = document.querySelector("#seleccionTarje");
let formularioPago = document.getElementById("formulario-tarjetas");

formaPag.addEventListener("change", (event) => {
    //se verifica la seleccion del metodo de pago
    let valorSeleccionado = event.target.value

    if (valorSeleccionado === "tarjecredi") {
        espacioTarjetas.style.display = 'block';

        
    } else {
        espacioTarjetas.style.display = 'none';
        mostrarBotones(false);
    }

    if (valorSeleccionado === "PagoBancos"){
        espacioBancos.style.display = 'block';
    } else {
        espacioBancos.style.display = 'none';
    }

    if (valorSeleccionado === "TransfBanc"){
        espacioTranferencias.style.display = 'block';
    } else {
        espacioTranferencias.style.display = 'none';
    }
})


function pagoTarjetas (seleccionTarjeta) {

    let espacioDocs = document.getElementById("campos-tarjeta");

    if(seleccionTarjeta.value !== "0"){

        if(!espacioDocs && espacioTarjetas){
                //creamos campo
                espacioDocs = document.createElement("div");
                espacioDocs.setAttribute("id","campos-tarjeta");
                //creamos titulo
                let titulocampo = document.createElement("h3");
                titulocampo.setAttribute("id","titulo-numero-tarjeta");
                titulocampo.textContent = "Ingrese el numero de su tarjeta "+seleccionTarjeta.value; 
                
                //creamos input y le damos atributos
                let campoDigitos = document.createElement("input");
                campoDigitos.setAttribute("type","text");
                campoDigitos.setAttribute("id","input-numero-tarjeta");
                campoDigitos.classList.add("form-control");
                campoDigitos.classList.add("Input");

                // añadimos titulo e input al campo
                espacioDocs.appendChild(titulocampo);
                espacioDocs.appendChild(campoDigitos);

                //añadimos campo al div general
                espacioTarjetas.appendChild(espacioDocs);

                
                    let tituloFecha = document.createElement("h3");
                    tituloFecha.setAttribute("id", "titulo-fecha-vencimiento");
                    tituloFecha.textContent = "Ingrese la fecha de vencimiento de su tarjeta "+seleccionTarjeta.value;

                    let campoFecha = document.createElement("input");
                    campoFecha.setAttribute("type","month");
                    campoFecha.setAttribute("name","FechaVencimiento");
                    campoFecha.classList.add("form-control");
                    campoFecha.classList.add("Input");

                    espacioDocs.appendChild(tituloFecha);
                    espacioDocs.appendChild(campoFecha);

                    let tituloTitular = document.createElement("h3");
                    tituloTitular.setAttribute("id","titulo-nombre-titular");
                    tituloTitular.textContent = "Ingrese el nombre del titular de la tarjeta " + seleccionTarjeta.value;

                    let campoTitular = document.createElement("input");
                    campoTitular.setAttribute("type","text");
                    campoTitular.setAttribute("name","Nombre titular");
                    campoTitular.setAttribute("placeholder", "Ingrese el nombre completo del titular")
                    campoTitular.classList.add("form-control");
                    campoTitular.classList.add("Input");

                    espacioDocs.appendChild(tituloTitular);
                    espacioDocs.appendChild(campoTitular)
                    
                    crearBotonesPago();
                    mostrarBotones(true);
        }

        if (espacioDocs) {
            
            // 1. Obtener los elementos que necesitan actualización por ID
            let titulocampo = document.getElementById("titulo-numero-tarjeta");
            let tituloFecha = document.getElementById("titulo-fecha-vencimiento");
            let tituloTitular = document.getElementById("titulo-nombre-titular");
            let campoDigitos = document.getElementById("input-numero-tarjeta");
            
            let valorTarjeta = seleccionTarjeta.value;
            
            // 2. Actualizar el texto (título) con la tarjeta seleccionada
            if(titulocampo) titulocampo.textContent = `Ingrese el número de su tarjeta ${valorTarjeta}`; 
            if(tituloFecha) tituloFecha.textContent = `Ingrese la fecha de vencimiento de su tarjeta ${valorTarjeta}`;
            if(tituloTitular) tituloTitular.textContent = `Ingrese el nombre del titular de la tarjeta ${valorTarjeta}`;

            // 3. Actualizar la validación de dígitos según el tipo de tarjeta
            if (campoDigitos) {
                if (valorTarjeta == "American Express") {
                    campoDigitos.setAttribute("minlength", "15");
                    campoDigitos.setAttribute("maxlength", "15");
                    campoDigitos.setAttribute("placeholder", "Recuerda que son 15 dígitos");
                } else {
                    // Restablecer a 16 para Visa/Mastercard
                    campoDigitos.setAttribute("minlength", "16");
                    campoDigitos.setAttribute("maxlength", "16");
                    campoDigitos.setAttribute("placeholder", "Recuerda que son 16 dígitos");
                }
            }
            mostrarBotones(true);
        }
    } else {

        if(espacioDocs){
            espacioDocs.remove();
        }
        mostrarBotones(false)
    }

}

if(formularioPago)  {
    formularioPago.addEventListener("submit", (event) => {
        event.preventDefault();
    
        if(validarCampos()){
            const datosPago = {
                metodoPago: formaPag.value,
                tipoTarjeta: seleccionTarjeta ? seleccionTarjeta.value : "0",
                numeroTarjeta: document.getElementById("input-numero-tarjeta")?.value || "",
                fechaVencimiento: document.querySelector('input[name="FechaVencimiento"]')?.value || "",
                nombreTitular: document.querySelector('input[name="Nombre titular"]')?.value || "",
                banco: document.getElementById("seleccionBanco")?.value || "",
                transferencia: document.getElementById("seleccionTransf")?.value || "",
                timestamp: new Date().toISOString()
            };
            
            // Guardar en LocalStorage
            localStorage.setItem('datosPago', JSON.stringify(datosPago));
            
            alert("Procediendo con el pago...");
            window.open("pagoexitoso.html", "_blank");
        } else {
            alert("Por favor complete todos los campos requeridos");
        }
    })
}

function validarCampos () {
    let tipoTarjeta = seleccionTarjeta ? seleccionTarjeta.value : "0";

    if (tipoTarjeta == "0"){
        return true;
    }

    let campoNumero = document.getElementById("input-numero-tarjeta");
    let campoFecha = document.querySelector('input[name="FechaVencimiento"]');
    let campoTitular = document.querySelector('input[name="Nombre titular"]');

    let esValido = true;
    let mensajeError = "";

    if(!campoNumero || campoNumero.value.trim() == ""){
        mensajeError += "Falta ingresar el numero de la tarjeta";
        esValido= false;
    }

    if (!campoFecha || campoFecha.value.trim() === "") {
        mensajeError += "Falta ingresar la fecha de vencimiento. ";
        esValido = false;
    }

    if (!campoTitular || campoTitular.value.trim() === "") {
        mensajeError += "Falta ingresar el nombre del titular. ";
        esValido = false;
    }

     if (!esValido) {
        alert("Faltan campos por llenar: " + mensajeError);
        // O mejor: mostrar un mensaje de error dentro del formulario
    }

    return esValido;
}


function crearBotonesPago() {
    let botonExistente = document.getElementById("boton-submit");


    // Solo creamos si el formulario existe Y si los botones NO existen
    if (formularioPago && !botonExistente) {
        
        let botonSubmit = document.createElement("button");
        botonSubmit.textContent = "PAGAR";
        botonSubmit.setAttribute("type", "submit");
        botonSubmit.setAttribute("id", "boton-submit");
        botonSubmit.classList.add("btn", "btn-success", "m-2"); // Añadí margen
        
        let botonReset = document.createElement("button");
        botonReset.textContent = "BORRAR";
        botonReset.setAttribute("type", "reset"); 
        botonReset.setAttribute("id", "boton-reset");
        botonReset.classList.add("btn", "btn-danger", "m-2");

        // Añadimos los botones al formulario
        formularioPago.appendChild(botonSubmit);
        formularioPago.appendChild(botonReset);
    } 
    
}

function mostrarBotones(mostrar) {
    const displayStyle = mostrar ? 'block' : 'none';
    const botonSubmit = document.getElementById("boton-submit");
    const botonReset = document.getElementById("boton-reset");

    if (botonSubmit) botonSubmit.style.display = displayStyle;
    if (botonReset) botonReset.style.display = displayStyle;
}