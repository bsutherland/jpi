package models

case class JapaneseAddress(
  postalCode: String,
  prefectureKana: String,
  municipalityKana: String,
  neighbourhoodKana: String,
  prefectureKanji: String,
  municipalityKanji: String,
  neighbourhoodKanji: String
)
