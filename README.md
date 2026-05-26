## PlaylistMaker
Приложение помогает создавать плейлисты треков из музыкальной базы iTunes, применимо в сфере медиа и развлечений.

### Стек технологий
**Архитектура** - MVVM  
**DI** - Koin  
**Плеер для превью треков** - MediaPlayer  

#### UI
- Android View
- Fragments
- подход Single Activity
- ViewBinding
  
**UI Components** 
- RecyclerView
- ViewPager2
- BottomSheet
  
**Навигация** - Jetpack Navigation

#### Работа с iTunes API
**Сетевые запросы** - Retrofit2  
**Сериализация JSON** - Gson  
**Загрузка и отображение изображений** - Glide  

#### Хранение
- Room  
- SharedPreferences  
- Internal Storage  

#### Presentation
- ViewModel  
- LiveData  

#### Асинхронность
- Kotlin Coroutines  
- Coroutines Flow  
