## Содержание

* [Введение](#введение)
* [Отказ от ответственности](#отказ-от-ответственности)
* [Задачи](#задачи)
* [Как собирать/запускать?](#как-собиратьзапускать)
* [Русские буквы и C++](#русские-буквы-и-c)

## Введение

Решение всех задач (дополняется). Большинство задач написано на C/C++ (исключениями являются задача про автозамену и фильтр Блума, очень уж удобно их писать на Haskell). После названия задачи в скобках указан язык программирования, на котором она была сдана. Рядом с задачей написано о различных ловушках в тестах.

## Отказ от ответственности

Решения задач выложены только с целью предупредить следующие поколения о возможных странностях тестов ~~которые ни один человек не предугадает~~. Автор всё равно рекомендует сделать эти задачи самостоятельно, а если не получится, то только тогда смотреть готовое решение. __Списывать не рекомендуется, т. к. ваше решение будет дисквалифицировано и вы получите штраф по баллам и/или дизреспект преподавателя и/или 0 за модуль!__ Так что готовое решение лучше самому осмыслить и написать своё, оригинальное.

Что-то не нравится в решении(-ях)? Отлично, напишите своё(-и)! Повторюсь, эти файлы нужно не просто переписать и сдать.

__Актуально на осенний семестр 2021-2022 учебного года.__

## Задачи

* Модуль 1
    * [Сумма (С)](module1/sum/main.c).
        В строке может быть не только число (и если вдруг будет, то не факт, что одно). __Такая же задача будет в тестовом модуле, так что можно протестировать там!__
    * [Стек (C++)](module1/stack/main.cpp).
        Бывают неправильные команды и всяческие отступления от формата ввода.
    * [Очередь (C++)](module1/queue/main.cpp).
        То же самое, что и в задаче про стек.
    * [Граф (C++)](module1/graph/main.cpp).
        Тут без приколов.
* Модуль 2
    * [Косое дерево (C++)](module2/splay_tree/main.cpp).
        Если элемент не найден, поднимается вверх последний элемент в пути, по которому искали. И прочие подобные приколы. И над печатью дерева нужно очень хорошо подумать.
    * [Min-куча (C++)](module2/min_heap/main.cpp).
        Приколов не обнаружено.
    * [Автозамена (Haskell)](module2/autocorrection/Main.hs).
        Могут быть русские буквы, регистр которых надо понижать. В Haskell функция `Data.Char.toLower` действует с опорой на локаль, так что никаких проблем не возникнет, [чего не скажешь о C++](#русские-буквы-и-c).
* Модуль 3
    * [Задача о рюкзаке (C++)](module3/knapsack/main.cpp).
        Тут приколов сразу два. Первый - динамика над ценностями предметов. Второй - парсинг команд.
    * [Сумасшедший богач (C++)](module3/greedy/main.cpp).
        В правильных ответах нет действий, указано только их количество.
    * [Фильтр Блума (Haskell)](module3/bloom_filter/Main.hs).
        Из найденных пока приколов - операнды для вычисления хэш-функции стоит брать по модулю M, также присутствует абсолютно тупой ввод.

## Как собирать/запускать?

Ручками! Вооружиться любимым компилятором и вперёд. Или вставить код в любимую IDE.

Если вдруг второй семестр и ТиМП прошли мимо вас, это делается примерно так 

* для C++:

```bash
$ g++ -O3 -g -o main main.cpp
$ ./main
```

* для Haskell (_вы пишете на Haskell и не можете скомпилировать из командной строки?_) мало что отличается:

```bash
$ ghc Main.hs
$ ./Main
```

__P. S.__ Тестирование программ на языках C/C++ осуществляется с помощью valgrind. Из-за этого, например, Haskell без оптимизации работает быстрее C++ в задаче про автокоррекцию примерно в 200-300 раз. У себя запустить с помощью valgrind программу можно следующим образом:

```bash
$ valgrind --leak-check=full ./main
```

## Русские буквы и C++

Если вы собрались писать автозамену на C++, то ~~я вам сочувствую~~ вам придётся иметь дело с русскими буквами. Для этого придётся сделать что-то наподобие такого:

```cpp
#include <algorithm>
#include <functional>
#include <locale>
#include <iostream>
#include <string>

int main(int argc, char* argv[]) {
    // This is an example code which converts line from
    // stdin to lower case according to user locale
    std::ios_base::sync_with_stdio(false); // required for imbue() to work as intended
    std::locale loc{""}; // use user locale
    // Populate std::wcin and std::wcout with locale (i. e. tell 'em
    // how to deal with std::wstring)
    std::wcin.imbue(loc);
    std::wcout.imbue(loc);
    std::wstring s;
    std::getline(std::wcin, s);
    // Convert tolower case in-place
    std::transform(s.begin(), s.end(), s.begin(),
                   std::bind(std::tolower<wchar_t>, // use this template from <locale>
                             std::placeholders::_1,
                             std::cref(loc)));
    std::wcout << s << std::endl;
}
```
