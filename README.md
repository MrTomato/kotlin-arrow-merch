# What is this?

This is the code repo for the talk "Arrow - upgrade your funcy Kotlin!" at the Vienna Kotlin meetup on 2024-06-11.

kotlin-arrow-merch is the backend for my arrow merchandise drop shipping operation which will hopefully finance the
Lambo of my dreams: The red one.

Mistakes are made on purpose and PRs to fix them will be rejected.

Feel free to fork and continue development, but remember to link back to this and copy the license.

Enjoy.

## A sort of guide

Maybe first start by checking out `build.gradle.kts:22` that shows what this project contains.

Here are some starting points to where you can find problems that match arrowy solutions:

- `io/acme/arrow_merch/catalog/CatalogServiceTest.kt:60`:
  The exception handling kinda sucks. *Either* we fix it, or we will have some issues in production 😢
- `io/acme/arrow_merch/customers/repo/CustomerEntity.kt:33`:
  JPA doesn't work well with read-only data classes. We separated the JPA entities already, but the conversion from
  nullable to non-nullable does just throw random exceptions... we might want to *validate* that they are indeed set
  first 🤔
- `io/acme/arrow_merch/customers/CustomerTest.kt:88`:
  This nested copy of copy mess is horrible for two levels - god help us if we need more levels...
  maybe we can look at through a different *lense* to find a better way 🤓
- `io/acme/arrow_merch/catalog/CatalogIT.kt:176`:
  Some weird Tech mogul has taken over Kongo and decided to just turn off servers... they are still doing okay, but
  the flakyness is increasing. At some point he might just *cut the circuits* all together. We should have a fallback
  for that day 💥
- `io/acme/arrow_merch/catalog/CatalogService.kt:17`:
  Our CEO said something about 'massively scaling up the operation'... pretty sure he didn't mean scaling up the
  response times too... let's look at this later, we can't *parMap* all the issues at the same time! 🤯