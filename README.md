# Email-Filtering-using-Naive-Bayes-Text-Classification
• Implemented Email Filtering and filtered text into various categories like Spam, Social, Promotions etc using Naïve Bayes Text Classification.

In text classification, we are given a description d ∈ X of a document, where X is the document space; and a fixed set of classes C = {c1,c2,c3,...}. Classes are also called categories or labels.Typically, the document space X is some type of high-dimensional space, and the classes are human defined for the needs of an application. And for this application classes are Spam, Social, Promotions etc.

Using a learning method or learning algorithm, we then wish to learn a classifier or classification function g that maps documents to classes:
                                                        Y : X -> C
              
I have choosen Naive Bayes for solving Email Filtering problem.

Naive Bayes text classification:

It is a Probablistic Learning Model. The probability of a document d being in class c is computed as,
                      
                                          P(c/d) - > P(c) MUL(P(term(k)/c))                    
                                          1 <= k <= no. of docs
                                          
In text classification, our goal is to find the best class for the document. The best class in NB classification is the most likely or Maximum a Posteriori (MAP) class Cmap:

                                          Cmap = argmax P(c/d) = argmax P(c) * MUL(term(k)/c)
                                          1 <= k <= no. of docs
                                       

I first calculated the conditional probabilities: P(term(k)/c)

Also, calculated the prior probabilities for each class: P(c)

And, we decide whether the Class of new email based on which probablity is higher.

Also, applied Laplace smoothing to avoid the product of conditional probablities being zero.

Advantages:
Naive Bayes is suitable for Text Classification.

Dis-Advantages:
Naive Bayes pays no attention to ordering or context (words occurring together can have quite different meanings from occurring singly).
To avoid that we have to apply Sentiment Analysis.
