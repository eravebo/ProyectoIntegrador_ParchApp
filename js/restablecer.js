let campo1 = document.querySelector("#password1");
let campo2 = document.querySelector("#password2");
let botonEnviar = document.querySelector(".submit-btn");
let form = document.querySelector(".restablecimiento");
let divfinal = document.querySelector(".recovery-footer"); 



form.addEventListener("submit", (event) => {
    event.preventDefault();

    comparacion();
})

campo1.addEventListener("focus", () => {
    
})

function comparacion () {
    if (campo1.value && campo2.value){

        if(campo1.value.length < 8 && campo2.value.length < 8){ 
            alert("La contraseña debe tener al menos 8 caracteres");
        } else {
            if(campo2.value = campo1.value){
            
            alert("¡Contraseña reestablecida correctamente!");

            let msjIni = document.createElement("p");
            msjIni.textContent = "Dale click y dirigete a la pagina de inicio ☝";
            msjIni.style.color = "green";
            
            divfinal.appendChild(msjIni);

            }  else {
            alert("Las contraseñas no coinciden")
            }
        } 
    }else{
        alert("Debes llenar los dos campos")

    }    
}