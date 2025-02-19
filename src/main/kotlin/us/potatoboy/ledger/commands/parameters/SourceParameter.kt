package us.potatoboy.ledger.commands.parameters

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.command.CommandSource
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

class SourceParameter : SimpleParameter<String>() {
    override fun parse(stringReader: StringReader): String {
        val i: Int = stringReader.cursor

        while (stringReader.canRead() && isCharValid(stringReader.peek())) {
            stringReader.skip()
        }

        return stringReader.string.substring(i, stringReader.cursor)
    }

    private fun isCharValid(c: Char): Boolean = c in '0'..'9' || c in 'a'..'z' || c in 'A'..'Z' || c == '@' || c == '_'

    override fun getSuggestions(
        context: CommandContext<ServerCommandSource>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val stringReader = StringReader(builder.input)
        stringReader.cursor = builder.start

        val players = context.source.playerNames
        players.add("@")
        // TODO suggest non-player sources

        return CommandSource.suggestMatching(
            players,
            builder
        )
    }
}
