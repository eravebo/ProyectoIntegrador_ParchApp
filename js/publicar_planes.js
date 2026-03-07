document.addEventListener('DOMContentLoaded', function () {
    const PLANS_KEY = 'parchapp_agency_plans';

    const form = document.getElementById('planForm');
    const plansContainer = document.getElementById('plansContainer');

    function loadPlans() {
        try {
            const raw = localStorage.getItem(PLANS_KEY);
            return raw ? JSON.parse(raw) : [];
        } catch (e) {
            return [];
        }
    }

    function savePlans(plans) {
        try {
            localStorage.setItem(PLANS_KEY, JSON.stringify(plans));
        } catch (e) {
            // ignorar errores de almacenamiento
        }
    }

    function getDefaultImageForDestination(destination) {
        const map = {
            'Guatapé': 'https://cincohorizontes.com/wp-content/uploads/2021/10/La-Piedra-del-Penon-de-Guatape-al-fondo.jpg',
            'Jardín': 'https://visitarmedellin.com/wp-content/uploads/2024/05/Jardin-Antioquia.jpg',
            'Santa Fe de Antioquia': 'https://puebliandoporantioquia.com.co/wp-content/uploads/2019/07/Santa-Fe-de-Antioquia-a9.jpg',
            'Medellín': 'https://media.tacdn.com/media/attractions-splice-spp-674x446/06/dd/d4/1b.jpg',
            'Parque Arví': 'https://blog.redbus.co/wp-content/uploads/2024/10/Proyecto-nuevo-2025-02-27T112200.640.jpg',
            'Río Claro': 'https://www.viajescomfama.com/portals/1139/Images/rio-claro/viajescomfama-antioquia-viva-rio-claro.jpg',
            'San Jerónimo': 'https://periodicoeloccidental.com/wp-content/uploads/2024/04/img_4582-1.jpg',
            'Cerro Tusa': 'https://www.medellinadvisors.com/wp-content/uploads/2019/05/cerro-tusa-is-a-natural-sanctuary-of-medellin.jpg'
        };
        return map[destination] || 'https://via.placeholder.com/600x400?text=Plan+tur%C3%ADstico';
    }

    function createPlanCard(plan) {
        const card = document.createElement('div');
        card.className = 'card';

        const imageSrc = plan.imageDataUrl || getDefaultImageForDestination(plan.destination);

        card.innerHTML = ''
            + '<div class="card-header">' + (plan.destination || 'Destino') + '</div>'
            + '<div class="card-body">'
            + '  <div class="card-category">' + (plan.agency || 'Agencia') + '</div>'
            + '  <img src="' + imageSrc + '" alt="' + (plan.title || '') + '" style="width:100%;max-height:200px;object-fit:cover;margin:0.5rem 0;border-radius:4px;">'
            + '  <h3 class="card-title">' + (plan.title || 'Plan sin título') + '</h3>'
            + '  <p class="card-description">' + (plan.description || 'Sin descripción') + '</p>'
            + '  <p class="card-price">' + new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', minimumFractionDigits: 0 }).format(plan.price || 0) + '</p>'
            + '  <p class="card-date">Disponibilidad: ' + (plan.availability || 0) + ' cupos</p>'
            + '</div>';

        return card;
    }

    function renderPlans() {
        const plans = loadPlans();
        plansContainer.innerHTML = '';

        if (!plans.length) {
            const msg = document.createElement('p');
            msg.textContent = 'Aún no hay planes publicados. Crea el primero usando el formulario de arriba.';
            msg.style.color = '#333';
            plansContainer.appendChild(msg);
            return;
        }

        plans.forEach(function (plan) {
            plansContainer.appendChild(createPlanCard(plan));
        });
    }

    function handleSubmit(event) {
        event.preventDefault();

        const agencyName = document.getElementById('agencyName').value.trim();
        const planTitle = document.getElementById('planTitle').value.trim();
        const destination = document.getElementById('destination').value;
        const description = document.getElementById('description').value.trim();
        const priceValue = parseInt(document.getElementById('price').value, 10) || 0;
        const availabilityValue = parseInt(document.getElementById('availability').value, 10) || 0;
        const imageInput = document.getElementById('image');
        const file = imageInput.files[0];

        if (!planTitle || !destination || !priceValue || !availabilityValue) {
            alert('Por favor completa al menos el nombre del plan, destino, valor y disponibilidad.');
            return;
        }

        function savePlan(imageDataUrl) {
            const plans = loadPlans();
            plans.push({
                id: Date.now(),
                agency: agencyName,
                title: planTitle,
                destination: destination,
                description: description,
                price: priceValue,
                availability: availabilityValue,
                imageDataUrl: imageDataUrl || null
            });
            savePlans(plans);
            renderPlans();
            form.reset();
        }

        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                savePlan(e.target.result);
            };
            reader.readAsDataURL(file);
        } else {
            savePlan(null);
        }
    }

    if (form && plansContainer) {
        form.addEventListener('submit', handleSubmit);
        renderPlans();
    }
});

