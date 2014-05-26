
import glob
import random

if __name__ == '__main__':
    files = glob.glob('src/com/madhu/mr/*.java')
    files += glob.glob('src/com/madhu/mr/job/*.java')
    files += glob.glob('src/com/madhu/mr/view/*.java')

    files.sort()

    total = 0    
    for fn in files:
        n = 0
        f = open(fn)
        while f.readline():
            n += 1
        f.close()
        total += n
        print '%s: %d' % (fn, n)
    print 'Total: %d' % total
    
    suits = ['Hearts', 'Spades', 'Diamonds', 'Clubs']
    deck = []
    cards = range(2, 11)
    cards.append('Jack')
    cards.append('Queen')
    cards.append('King')
    cards.append('Ace')
    for suit in suits:
        for card in cards:
            deck.append('%s of %s' % (card, suit))
    random.shuffle(deck)
    for card in deck:
        print card
        