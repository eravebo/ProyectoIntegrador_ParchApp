let botonEnviar = document.querySelector(".submit-btn");
let formulario = document.querySelector(".recoveryForm");

formulario.addEventListener("submit", (event) => {
    event.preventDefault();
    envio();
})



function envio (){
    alert("Revisa la bandeja de entrada de tu correo electrónico para continuar");

    setTimeout(()=>{
        window.location.href = "restablecer.html";

    },500)
}