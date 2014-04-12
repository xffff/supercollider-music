/*
Event type for easy integration of conTimbre with SC.
Just supply an osc destination to the key \oscout.

For example:
	Pbind(
		\type, \ctevent,
		\oscout, NetAddr("127.0.0.1", 3000),
		\osccmd, \noteon,
		\voicename, \fl,
        \midinote, Prand((40..60),inf),
		\dur, Prand([1,2,1/2],inf),
		\amp, Pexprand(0.75,1.0,inf)
	)

Further information on specific commands can be found in the ePlayer manual.

Mike Murphy 2013
http://www.contimbre.com
*/

Event.addEventType(\ctevent, {|server|
	var freqs, lag, dur, sustain, strum;
	var bndl, oscout, hasGate, osccmd;
	var oscEventFunctions = (
		\noteon:
		#{ arg voicename, midinote=60, amp=0.1;
			[voicename, midinote, asInteger((amp * 127).clip(0, 127))] },
		\noteoff:
		#{ arg voicename, midinote=60;
			[voicename, midinote] },
		\detuned_noteon:
		#{ arg voicename, midinote=60, detune=0, amp=0.1;
			[voicename, midinote, detune, asInteger((amp * 127).clip(0, 127))] },
		\detuned_noteoff:
		#{ arg voicename, midinote=60, detune=0;
			[voicename, midinote, detune] },
		\ctnote:
		#{ arg voicenumber=0, noteid=0, midinote=60, amp=0.1, duration=0;
			[voicenumber, noteid, midinote, asInteger((amp * 127).clip(0, 127)), duration] },
		\ctnoteoff:
		#{ arg noteid=0;
			[noteid] },
		\glissando:
		#{ arg voicename, midinote=60, interval=0, duration=0;
			[voicename, midinote, interval.clip(-24,24), duration] },
		\ctglissando:
		#{arg noteid, interval=0, duration=0;
			[noteid, interval.clip(-24,24), duration] },
		\detuned_glissando:
		#{arg voicename, midinote=60, detune=0, interval=0, duration=0;
			[voicename, midinote, detune, interval.clip(-24,24), duration] },
		\voice_glissando:
		#{arg voicename, interval=0, duration=0;
			[voicename, interval.clip(-24,24), duration] },
		\reverb_state:
		#{arg onoff=0; [onoff]},
		\reverb_gain:
		#{arg gain=0; [gain]},
		\reverb_out:
		#{arg output=0, onoff=0; [output,onoff]},
		\reverb_voicegain:
		#{arg index=0, gain=0; [index,gain]},
		\reverb_time:
		#{arg time=1.5; [time]},
		\reverb_roomsize:
		#{arg size=80; [size]},
		\reverb_damping:
		#{arg damping=0.75; [damping]},
		\let_vibrate:
		#{arg midinote=60, fullshort=0.5; [midinote,fullshort]},
		\program:
		#{ arg voicename, programname;
			[ voicename, programname ] },
		\gain:
		#{ arg voicename, gain=0; // this is in dB
			[ voicename, gain ] },
		\chordon:
		#{ arg voicename, pitchlist;
			[ voicename, pitchlist ] },
		\allNotesOff:
		#{ arg null=0; [null] },
		\kammerton:
		#{ arg freq=442; [freq]},
		\rest:
		#{ arg null=0; [null]}
	);

	freqs = ~freq = ~detunedFreq.value;

	if (freqs.isRest.not) {
		~amp = ~amp.value;
		~midinote = freqs.cpsmidi;
		strum = ~strum;
		lag = ~lag;
		sustain = ~sustain = ~sustain.value;
		oscout = ~oscout.value; // OSC address
		hasGate = ~hasGate ? true;
		osccmd = ~osccmd;
		bndl = oscEventFunctions[osccmd].valueEnvir.asCollection;

		bndl = bndl.asControlInput.flop;

		bndl.do {|msgArgs, i|
			var latency;
			var message;

			message = Array.newClear(msgArgs.size+1);
			message[0]=osccmd.asString;
			msgArgs.size.do{|i| message[i+1]=msgArgs[i]};

			latency = i * strum + lag;

			// don't send anything if type is \rest
			if(osccmd!=\rest,{
				if(latency == 0.0) {
					oscout.sendBundle(latency, message)
				} {
					thisThread.clock.sched(latency, {
						oscout.sendBundle(latency, message)
					})
				}
			});

			// automatically deal with noteoff messages for each note-type
			case
			{hasGate and: { osccmd === \noteon }} {
				message[0]="noteoff";
				thisThread.clock.sched(sustain + latency, {
					oscout.sendBundle(latency, message)
			})}
			{hasGate and: { osccmd === \detuned_noteon }} {
				message[0]="detuned_noteoff";
				thisThread.clock.sched(sustain + latency, {
					oscout.sendBundle(latency, message)
			})}
			{hasGate and: { osccmd === \ctnote }} {
				message[0]="ctnoteoff";
				thisThread.clock.sched(sustain + latency, {
					oscout.sendBundle(latency, message)
			})};
		};
	}
});

