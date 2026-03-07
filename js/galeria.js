let tourTomado = document.querySelector("#location");
let foto = document.querySelector("#photo");
let botonSubir = document.querySelector("#subir");
let espacioGaleria = document.querySelector("#galeria-espacio");
let descripcion = document.querySelector("#description");

botonSubir.addEventListener("click", function(event) {
    event.preventDefault();
    subirFoto();
});

document.addEventListener("DOMContentLoaded", () => {
    mostrarPublicaciones();
})

foto.addEventListener('change', function(e) {
    const fileName = e.target.files[0] ? e.target.files[0].name : 'Ningún archivo seleccionado';
    

    // Debug en consola
    console.log('Archivos seleccionados:', e.target.files);

});

function subirFoto() {
    let tour = tourTomado.value;
    let archivoFoto = foto.files[0]; 
    let descripcionValor = descripcion.value; // Obtener el valor de la descripción

    // 1. Validar que los campos necesarios estén llenos
    if (tour && archivoFoto && descripcionValor) {

        // 2. Crear una instancia de FileReader
        const reader = new FileReader();

        // 3. Definir qué hacer cuando el archivo termine de leerse
        reader.onload = function(e) {
            // e.target.result contiene la Data URL (Base64) de la imagen
            const imageUrl = e.target.result;

            let publicacion = {
                tour: tour,
                foto: imageUrl,
                descripcion: descripcionValor // Incluir la descripción en el objeto
            };

            console.log("Nueva publicación:", publicacion);
            let publicaciones = JSON.parse(localStorage.getItem("publicaciones")) || [];
            publicaciones.push(publicacion);
            localStorage.setItem("publicaciones", JSON.stringify(publicaciones));
            alert("Foto subida exitosamente!");

            

            // 4. Agregar la nueva tarjeta al espacio de la galería
            espacioGaleria.innerHTML += `
            <div class="card" style="width: 18rem; margin: 15px; display: inline-block;">
                <img src="${imageUrl}" class="card-img-top" alt="Foto de ${tour}">
                <div class="card-body">
                    <h5 class="card-title">${tourTomado.options[tourTomado.selectedIndex].text}</h5>
                    <p class="card-text">${descripcionValor}</p>
                </div>
            </div>`;
        };

        // 5. Iniciar la lectura del archivo como Data URL
        reader.readAsDataURL(archivoFoto);

        tourTomado.value = "";
            foto.value = "";
            descripcion.value = "";

    } else {
        alert("Por favor, completa todos los campos (Destino, Foto y Descripción) antes de subir la foto.");
        // Opcional: limpiar los campos si quieres forzar la re-selección.
        return;
    }
}


function mostrarPublicaciones() {
    let publicaciones = JSON.parse(localStorage.getItem("publicaciones")) || [];
    espacioGaleria.innerHTML = ""; // Limpiar el espacio de la galería antes de mostrar
    publicaciones.forEach(pub => {
        espacioGaleria.innerHTML += `
        <div class="card" style="width: 18rem; margin: 15px; display: inline-block;">
            <img src="${pub.foto}" class="card-img-top" alt="Foto de ${pub.tour}">
            <div class="card-body">
                <h5 class="card-title">${pub.tour}</h5>
                <p class="card-text">${pub.descripcion}</p>
            </div>
        </div>`;
    });
}