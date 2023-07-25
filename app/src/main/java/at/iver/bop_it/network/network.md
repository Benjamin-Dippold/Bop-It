# Communication

## Messages

### Client
- event(CardID, targetDeck)
- choice(String)
- endTurn()

### Server
- add(CardID, targetDeck)
- remove(CardID, targetDeck)
- update(PlayerStats)
- choose(List<String>)

## Sequence Diagram

```mermaid
sequenceDiagram
    actor U as User
    participant C as Client
    participant S as Server
    participant GS as GameSession
    participant PA as PlayArea

    U ->> C: initiates Game
    C ->> S:requests card-decks
    S -->> C: card-decks

    alt play card
    U ->> C: touch handcard
    C ->> S: msg(cardID, handDeck)
    activate S
    S ->> GS: getCurrentPlayer()
    GS -->> S: return(currentPlayer)
    note over S: verify current Player
    S ->> PA: playCard(CardID)
    PA -->> S: return(Card)
    note over S: verify card played
    S ->> C: remove(CardID, handDeck)
    S ->> C: add(CardID, playArea)
    C -->> S: confirm()
    deactivate S

    else buy card
    U ->> C: touch market card
    C ->> S: msg(cardID, marketDeck.forPurchase)
    activate S
    S ->> GS: getCurrentPlayer()
    GS -->> S: return(currentPlayer)
    note over S: verify current Player
    S ->> PA: buyCard(CardID)
    PA -->> S: return(Card)
    note over S: catch exception not enough money
    S ->> C: remove(CardID, marketDeck.forPurchase)
    S ->> C: add(CardID, discardedDeck)
    C -->> S: confirm()
    deactivate S
    else activate special-ability
    note over U, PA: tbd

    else expend ability
    note over U, PA: tbd

    else attack guard
    note over U, PA: tbd

    else end turn
    U ->> C: touch "Finish" button
    C ->> S: msg(endTurn)
    activate S
    S ->> GS: getCurrentPlayer()
    GS -->> S: return(currentPlayer)
    note over S: verify current Player
    S ->> GS: endTurn()
    GS -->> S: return(nextPlayer)
    S ->> C: statUpdate(playerstats)
    deactivate S

    end

```
